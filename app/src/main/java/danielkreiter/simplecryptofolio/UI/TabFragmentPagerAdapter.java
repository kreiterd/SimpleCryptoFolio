package danielkreiter.simplecryptofolio.UI;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import danielkreiter.simplecryptofolio.UI.Activity.AddPurchaseFragment;
import danielkreiter.simplecryptofolio.UI.Activity.BasicFragment;
import danielkreiter.simplecryptofolio.UI.Activity.ValueChartFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Übersicht", "Tab2", "Tab3"};
    private Context context;

    public TabFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        //  return CurrentValueFragment.newInstance(position + 1);

        switch (position) {
            case 0:
                return ValueChartFragment.newInstance(position + 1);
            case 1:
                return AddPurchaseFragment.newInstance(position + 1);
            default:
                return BasicFragment.newInstance(position + 1);

            // Other fragments
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}