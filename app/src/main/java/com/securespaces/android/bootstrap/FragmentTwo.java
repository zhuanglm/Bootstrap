package com.securespaces.android.bootstrap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentTwo extends InsetFragment {
    FeatureAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_two, container, false);
        setupInsetListener();

        ListView listView = (ListView)mView.findViewById(R.id.listView);

        mAdapter = new FeatureAdapter(getResources().getStringArray(R.array.space_features));
        listView.setAdapter(mAdapter);

        ImageView backButton = (ImageView) mView.findViewById(R.id.backImage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        TextView toolbarTitle = (TextView)mView.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.fragment2_title);

        return mView;
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
