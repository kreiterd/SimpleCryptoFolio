package danielkreiter.simplecryptofolio.UI;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;

public class PurchaseOverviewAdapter extends ArrayAdapter<Purchase> {

    private final Activity context;
    List<Purchase> mPurchases;
    List<Purchase> mChecked;


    public PurchaseOverviewAdapter(Activity context, List<Purchase> purchases) {
        super(context, R.layout.purchase_overview_listitem, purchases);
        this.context = context;
        this.mPurchases = purchases;
        mChecked = new ArrayList<>();
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {

            convertView = (context.getLayoutInflater()).inflate(
                    R.layout.purchase_overview_listitem, null);

            viewHolder = new ViewHolder();
            viewHolder.id = convertView.findViewById(R.id.id_textview);
            viewHolder.currencytype = convertView.findViewById(R.id.currencytype_textview);
            viewHolder.date = convertView.findViewById(R.id.date_textview);
            viewHolder.value = convertView.findViewById(R.id.value_textview);
            viewHolder.amount = convertView.findViewById(R.id.amount_textview);
            viewHolder.pricepercoin = convertView.findViewById(R.id.pricepercoin_textview);
            viewHolder.checked = convertView.findViewById(R.id.checked_checkbox);
            viewHolder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    Purchase purchase = mPurchases.get(getPosition);
                    // Boolean isChecked = buttonView.isChecked();
                    purchase.setChecked(isChecked);

                  /*
                   * better performance but higher code complexity
                   *

                   if (isChecked && !mChecked.contains(purchase)) {
                        mChecked.add(purchase);
                    }
                    if (!isChecked && mChecked.contains(purchase)) {
                        mChecked.remove(purchase);
                    }

                    */

                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.id_textview, viewHolder.id);
            convertView.setTag(R.id.currencytype_textview, viewHolder.currencytype);
            convertView.setTag(R.id.date_textview, viewHolder.date);
            convertView.setTag(R.id.value_textview, viewHolder.value);
            convertView.setTag(R.id.amount_textview, viewHolder.amount);
            convertView.setTag(R.id.pricepercoin_textview, viewHolder.pricepercoin);
            convertView.setTag(R.id.checked_checkbox, viewHolder.checked);

            convertView.setTag(R.id.checked_checkbox, viewHolder.checked);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.checked.setTag(position);

        Purchase purchase = mPurchases.get(position);
        viewHolder.id.setText(String.valueOf(purchase.getId()));

        viewHolder.id.setText("" + String.valueOf(purchase.getId()));
        viewHolder.currencytype.setText(" " + purchase.getCurrencytype());
        viewHolder.date.setText(" " + purchase.getDate());
        viewHolder.value.setText("");
        viewHolder.amount.setText(String.valueOf(purchase.getAmount()));
        viewHolder.pricepercoin.setText("");

        viewHolder.checked.setChecked(purchase.isChecked());

        return convertView;

    }

    public List<Purchase> getAllChecked() {
        updateChecked();
        return mChecked;
    }

    private void updateChecked() {
        mChecked.clear();
        for (Purchase p : mPurchases) {
            if (p.isChecked()) this.mChecked.add(p);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        updateChecked();
        super.notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView id;
        TextView currencytype;
        TextView date;
        TextView value;
        TextView amount;
        TextView pricepercoin;
        CheckBox checked;
    }
}