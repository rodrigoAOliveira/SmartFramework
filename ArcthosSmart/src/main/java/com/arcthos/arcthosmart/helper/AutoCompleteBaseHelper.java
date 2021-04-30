package com.arcthos.arcthosmart.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import com.arcthos.arcthosmart.shared.viewmodel.BaseViewModel;


/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 23/01/2019
 * Empresa : TOPi
 * ************************************************************
 */
public abstract class AutoCompleteBaseHelper<T, S extends BaseViewModel> {
    private AutoCompleteTextView autoCompleteTextView;
    private S viewModel;
    private Context context;
    private AutocompleteListener<T> listener;

    protected AutoCompleteBaseHelper(Context context, AutoCompleteTextView autoCompleteTextView, AutocompleteListener<T> listener) {
        this.autoCompleteTextView = autoCompleteTextView;
        this.viewModel = initViewModel(((FragmentActivity) context));
        this.context = context;
        this.listener = listener;
    }

    public void build() {
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 3) {

                    List<T> items = getItems(viewModel, charSequence.toString());
                    AutocompleteAdapter AutocompleteAdapter = new AutocompleteAdapter(context, items, listener);

                    autoCompleteTextView.setAdapter(AutocompleteAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public interface AutocompleteListener<T> {
        void onItemSelected(T item);
    }

    private class AutocompleteAdapter extends ArrayAdapter<T> implements Filterable {

        private AutocompleteListener<T> listener;

        private List<T> items;
        private List<T> filteredData = new ArrayList<>();
        private ItemFilter filter = new ItemFilter();

        public AutocompleteAdapter(@NonNull Context context, @NonNull List<T> items, AutocompleteListener<T> listener) {
            super(context, 0, 0, items);
            this.listener = listener;
            this.items = items;
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            T item = getItem(position);
            View listItem = convertView;
            if (listItem == null)
                listItem = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
            ((TextView) listItem.findViewById(android.R.id.text1)).setText(getDisplayText(item));
            listItem.setOnClickListener(v -> {
                if (listener != null) listener.onItemSelected(item);
                autoCompleteTextView.dismissDropDown();
            });
            return listItem;
        }

        public int getCount() {
            if (filteredData == null) return 0;
            return filteredData.size();
        }

        public T getItem(int position) {
            if (filteredData == null) return null;
            return filteredData.get(position);
        }

        @NonNull
        @Override
        public ItemFilter getFilter() {
            return filter;
        }

        private class ItemFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final ArrayList<T> resultList = new ArrayList<>();

                for (int i = 0; i < items.size(); i++) {
                    T item = items.get(i);
                    String filteredValue = getFilteredValue(item);
                    if (filteredValue.contains(filterString)) {
                        resultList.add(item);
                    }
                }

                results.values = resultList;
                results.count = resultList.size();

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (ArrayList<T>) results.values;
                notifyDataSetChanged();
            }


        }
    }

    protected abstract S initViewModel(FragmentActivity context);

    protected abstract String getDisplayText(T item);

    protected abstract List<T> getItems(S viewModel, String query);

    protected abstract String getFilteredValue(T item);
}
