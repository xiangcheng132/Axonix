from ultralytics import YOLO
import cv2
import numpy as np
from collections import deque

# 加载自定义YOLO模型
model = YOLO("lumos.pt")
model.to('cuda')  # 移动到cuda设备

# 区域参数配置
BOTTOM_WIDTH_RATIO = 0.5
TOP_WIDTH_RATIO = 0.25
TRAPEZOID_HEIGHT = 0.7

# 初始化视频流
cap = cv2.VideoCapture(0)

def get_trapezoid_bounds(h, w):
    """生成梯形区域及关键边界"""
    bottom_width = w * BOTTOM_WIDTH_RATIO
    top_width = w * TOP_WIDTH_RATIO
    trapezoid_h = int(h * TRAPEZOID_HEIGHT)

    trapezoid = np.array([
        [(w - bottom_width)//2, h],          # 左下
        [(w + bottom_width)//2, h],          # 右下
        [(w + top_width)//2, h - trapezoid_h],  # 右上
        [(w - top_width)//2, h - trapezoid_h]   # 左上
    ], dtype=np.int32)

    # 计算关键边界值
    boundaries = {
        'left': trapezoid[0][0],          # 左侧边界x坐标
        'right': trapezoid[1][0],         # 右侧边界x坐标
        'top_y': trapezoid[3][1]         # 梯形顶部y坐标
    }
    return trapezoid, boundaries

def analyze_box(box, trapezoid, boundaries):
    """分析单个检测框的位置特征"""
    x, y, w, h = box
    points = [
        (x - w/2, y - h/2),  # 左上角
        (x + w/2, y - h/2),  # 右上角
        (x + w/2, y + h/2),  # 右下角
        (x - w/2, y + h/2)   # 左下角
    ]

    # 初始化特征标志
    features = {
        'in_trapezoid': True,
        'left_out': False,
        'right_out': False,
        'upper_out': False,
        'partial_in': False
    }

    # 检查所有角点
    for (px, py) in points:
        # 梯形内检测
        if cv2.pointPolygonTest(trapezoid, (px, py), False) < 0:
            features['in_trapezoid'] = False

            # 左侧越界
            if px < boundaries['left']:
                features['left_out'] = True
            # 右侧越界
            elif px > boundaries['right']:
                features['right_out'] = True
            # 上部越界（y坐标小于梯形顶部）
            if py < boundaries['top_y']:
                features['upper_out'] = True
        else:
            features['partial_in'] = True

    return features

for result in model.predict(source=0, show=False, stream=True, device='mps'):
    frame = result.orig_img
    h, w = frame.shape[:2]

    # 获取梯形区域和边界
    trapezoid_pts, bounds = get_trapezoid_bounds(h, w)

    # 可视化区域
    cv2.polylines(frame, [trapezoid_pts], True, (0,255,0), 2)

    # 初始化状态参数
    status_info = {
        'total': 0,
        'left_out': False,
        'right_out': False,
        'upper_out': False,
        'partial_in': False,
        'full_in': False
    }

    if result.boxes:
        boxes = result.boxes.xywh.cpu().numpy()
        status_info['total'] = len(boxes)

        for box in boxes:
            features = analyze_box(box, trapezoid_pts, bounds)

            # 更新全局状态
            status_info['left_out'] |= features['left_out']
            status_info['right_out'] |= features['right_out']
            status_info['upper_out'] |= features['upper_out']
            status_info['partial_in'] |= features['partial_in']
            status_info['full_in'] |= features['in_trapezoid']

    # 状态判断逻辑
    if status_info['total'] == 0:
        status = "no detection"
        color = (0, 0, 255)
    else:
        # 轻微偏移判断
        if status_info['partial_in'] and status_info['left_out'] and status_info['upper_out']:
            status = "left"
            color = (0, 255, 255)
        elif status_info['partial_in'] and status_info['right_out'] and status_info['upper_out']:
            status = "right"
            color = (0, 255, 255)
        # 严重偏移判断
        elif status_info['left_out'] and status_info['upper_out']:
            status = "warning:left!"
            color = (0, 0, 255)
        elif status_info['right_out'] and status_info['upper_out']:
            status = "warning:right!"
            color = (0, 0, 255)
        # 转向判断
        elif status_info['partial_in'] and status_info['left_out'] and not status_info['upper_out']:
            status = "turn left"
            color = (255, 0, 0)
        elif status_info['partial_in'] and status_info['right_out'] and not status_info['upper_out']:
            status = "turn right"
            color = (255, 0, 0)
        # 结束判断
        elif status_info['full_in'] and not (status_info['left_out'] or status_info['right_out'] or status_info['upper_out']):
            status = "stop"
            color = (255, 255, 0)
        else:
            status = "GO"
            color = (0, 255, 0)

    # 添加状态提示
    cv2.putText(frame, status, (20, 50),
                cv2.FONT_HERSHEY_SIMPLEX, 1, color, 2)

    # 显示处理结果
    cv2.imshow('Blind Path Detection', frame)



    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()