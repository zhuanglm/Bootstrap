package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentOne extends BootstrapFragment {
    FeatureAdapter mAdapter;

    public static FragmentOne newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(BootstrapFragment.KEY_POSITION, position);
        FragmentOne fragment = new FragmentOne();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        mToolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText(R.string.fragment1_title);

        ListView listView = (ListView)view.findViewById(R.id.listView);

        mAdapter = new FeatureAdapter(getResources().getStringArray(R.array.space_features));
        listView.setAdapter(mAdapter);

        Button proceedButton = (Button)view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProceed();
            }
        });

        return view;
    }

    public class FeatureAdapter extends BaseAdapter {
        String[] mStrings;

        public FeatureAdapter(String[] strings) {
            mStrings = strings;
        }


        @Override
        public int getCount() {
            return mStrings.length;
        }

        @Override
        public String getItem(int position) {
            return mStrings[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView text;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_feature_point, null);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.textView2);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(getItem(position));

            return convertView;
        }
    }
}
