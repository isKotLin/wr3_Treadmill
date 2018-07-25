package com.vigorchip.treadmill.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 首页适配器
 */
public class HomePageAdapter extends FragmentPagerAdapter {
    List<Fragment> list;
FragmentManager fm;
    public HomePageAdapter(FragmentManager fms,List<Fragment> list) {//写构造方法，方便赋值调用
        super(fms);
        this.list=list;
        fm=fms;
    }
    @Override
    public Fragment getItem(int arg0) {//根据Item的位置返回对应位置的Fragment，绑定item和Fragment
        return list.get(arg0);
    }


    @Override
    public int getCount() {
        return list.size();
    }//设置Item的数量

//
//    public Object instantiateItem(ViewGroup container, int position) {
//        tagLists.add(makeFragmentName(container.getId(),
//                (int) getItemId(position)));
//        return super.instantiateItem(container, position);
//    }
//    public static String makeFragmentName(int viewId, int index) {
//        return "android:switcher:" + viewId + ":" + index;
//    }private List<String> tagLists;
//    public void update(int item) {
//        Fragment fragment = fm.findFragmentByTag(tagLists.get(item));
//        if (fragment != null) {
//            switch (item) {
//                case 0:
//                    break;
//                case 1:
//                    ((AppHomePageFragment) fragment).query();
//                    break;
//                case 2:
//                    ((SportHomePageFragment) fragment).query();
//                    break;
//                case 3:
//                    break;
//            }
//        }
//    }



//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        iv = new ImageView(mContext);
//        iv.setTag(position); // Add tag
//        try {
//            Bitmap bm = cacheImg2(position);
//            iv.setImageBitmap(bm);
//        } catch (OutOfMemoryError e) {
//            e.printStackTrace();
//        }
//        ((ViewPager)container).addView(iv);
//        return iv;
//    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        View view = (View)object;
//        int currentPage = ((DispImgActivity)mContext).getCurrentPagerIdx(); // Get current page index
//        if(currentPage == (Integer)view.getTag()){
//            return POSITION_NONE;
//        }else{
//            return POSITION_UNCHANGED;
//        }
////      return POSITION_NONE;
//    }




    //解决数据不刷新的问题
//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

}
