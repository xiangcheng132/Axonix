package com.Axonix.navmain;

import android.content.Context;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import java.util.List;
import com.amap.api.services.core.AMapException;


public class SearchManager {
    private Context context;

    public SearchManager(Context context) {
        this.context = context;
    }

    public interface SearchResultListener {
        void onSearchResult(List<Tip> tips);
        void onFailure(AMapException e);
    }

    public void searchPOI(String keyword, LatLonPoint center, SearchResultListener listener) {
        InputtipsQuery query = new InputtipsQuery(keyword, center != null ? center.toString() : "");
        Inputtips inputTips = new Inputtips(context, query);
        inputTips.setInputtipsListener(new Inputtips.InputtipsListener() {
            @Override
            public void onGetInputtips(List<Tip> tips, int resultCode) {
                if (resultCode == 1000 && tips != null) {
                    listener.onSearchResult(tips);
                }
            }
        });
        inputTips.requestInputtipsAsyn();
    }
}
