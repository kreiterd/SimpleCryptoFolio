package danielkreiter.simplecryptofolio.UI.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import danielkreiter.simplecryptofolio.UI.Fragments.AddPurchaseFragment;
import danielkreiter.simplecryptofolio.UI.Fragments.AllPurchasesFragment;
import danielkreiter.simplecryptofolio.UI.Fragments.BasicFragment;
import danielkreiter.simplecryptofolio.UI.Fragments.ValueChartFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Overview", "Add", "Delete", "Settings"};
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
            case 2:
                return AllPurchasesFragment.newInstance(position + 1);
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