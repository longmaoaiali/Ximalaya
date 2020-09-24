package com.cvte.ximalaya.api;

import com.cvte.ximalaya.utils.Constants;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2020/9/24.
 */

public class XimalayaDataApi  {

    private XimalayaDataApi(){};

    private static XimalayaDataApi sXimalayaDataApi;

    public static XimalayaDataApi getXimalayaDataApi(){
        if (sXimalayaDataApi == null) {
            synchronized (XimalayaDataApi.class) {
                if (sXimalayaDataApi==null) {
                    sXimalayaDataApi = new XimalayaDataApi();
                }
            }
        }

        return sXimalayaDataApi;
    }

    /**
     * 得到推荐的专辑列表
     * @param callBack 请求结果的回调接口
     */
    public void getRecommendList(IDataCallBack<GussLikeAlbumList> callBack){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMAND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map,callBack);
    }

    /**
     *
     * @param callBack 根据专辑ID获取专辑详情的回调接口
     * @param albumId 专辑ID
     * @param pageIndex 页码
     */
    public void getAlbumDetail(IDataCallBack<TrackList> callBack,int albumId,int pageIndex){
        Map<String,String> map = new HashMap<String,String>();
        map.put(DTransferConstants.ALBUM_ID,albumId+"");
        map.put(DTransferConstants.SORT,"asc");
        map.put(DTransferConstants.PAGE,pageIndex+"");
        CommonRequest.getTracks(map,callBack);
    }


}
