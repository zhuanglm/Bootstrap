package com.securespaces.android.bootstrap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentTwo extends Fragment {
    FeatureAdapter mAdapter;

    public View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        ListView listView = (ListView)view.findViewById(R.id.listView);

        mAdapter = new FeatureAdapter(getResources().getStringArray(R.array.space_features));
        listView.setAdapter(mAdapter);


        ImageView backButton = (ImageView) view.findViewById(R.id.backImage);
        backButton.setOnClickListener(mBackListener);

        TextView toolbarTitle = (TextView)view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.fragment2_title);
        toolbarTitle.setOnClickListener(mBackListener);

        Button proceedButton = (Button)view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BootstrapActivity)getActivity()).onProceedPushed(1);
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
