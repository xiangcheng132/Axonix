import axios from 'axios';

const API_URL = 'http://localhost:8080/friend-relationships';

export default {
  getFriendRelationshipCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getFriendRelationships(example) {
    return axios.post(`${API_URL}/list`, example);
  },
  getFriendRelationshipById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addFriendRelationship(friendRelationship) {
    return axios.post(`${API_URL}/`, friendRelationship);
  },
  addFriendRelationshipSelective(friendRelationship) {
    const now = new Date().toISOString(); // 获取当前时间
    return axios.post(`${API_URL}/selective`, {
      ...friendRelationship,
      createdtime: now, // 设定创建时间
      updatedtime: now  // 设定更新时间
    });
  },
  updateFriendRelationship(id, friendRelationship) {
    return axios.put(`${API_URL}/${id}`, friendRelationship);
  },
  updateFriendRelationshipSelective(id, friendRelationship) {
    return axios.patch(`${API_URL}/${id}/selective`, {
      ...friendRelationship,
      updatedtime: new Date().toISOString() // 只更新更新时间
    });
  },
  
  deleteFriendRelationship(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
