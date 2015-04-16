package com.grogshop.manage.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grogshop.manage.R;
import com.grogshop.manage.adapter.ListImgDirPopupWindowAdapter;
import com.grogshop.manage.domain.ImageFloder;
import com.grogshop.manage.ui.ChooseMorePictureActivity;

import java.util.List;

/**
 * Created by jiamengyu on 4/8/2015.
 */
public class ListImageDirPopupWindow extends BasePopupWindowForListView {

    private ListView mListDir;
    private Context mContext;
    ImageLoaderUtil imageLoaderUtil;
    ListImgDirPopupWindowAdapter mListImgDirPopupWindowAdapter;

    public ListImageDirPopupWindow(Context context, int width, int height,
                                   List<ImageFloder> datas, View convertView)
    {
        super(convertView, width, height, true, datas);
        this.mContext = context;
        imageLoaderUtil = new ImageLoaderUtil(mContext);
        mListImgDirPopupWindowAdapter = new ListImgDirPopupWindowAdapter(mContext,mDatas);
        initViews();
        initEvents();
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.id_list_dir);
        mListDir.setAdapter(mListImgDirPopupWindowAdapter);
    }

    public interface OnImgDirSelected{
        void selected(ImageFloder imageFloder);
    }

    private OnImgDirSelected mImgDirSelected;

    public void setOnImgDirSelected(OnImgDirSelected onImgDirSelected) {
        this.mImgDirSelected = onImgDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mImgDirSelected != null){
                    mImgDirSelected.selected((ImageFloder) mDatas.get(position));
                }
            }
        });
    }

    @Override
    public void init() {

    }
    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {

    }

}
