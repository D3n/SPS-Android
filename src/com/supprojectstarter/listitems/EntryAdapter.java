package com.supprojectstarter.listitems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.supprojectstarter.sps.R;

import java.util.ArrayList;

public class EntryAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> items;
    private LayoutInflater vi;

    public EntryAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(R.layout.categories_separator_layout, null);
                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);
                final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getTitle());
            } else {
                EntryItem ei = (EntryItem) i;
                v = vi.inflate(R.layout.project_tile_layout, null);
                final TextView title = (TextView) v.findViewById(R.id.tile_title_tv);
                final TextView percentage = (TextView) v.findViewById(R.id.tile_percentage_tv);
                final TextView description = (TextView) v.findViewById(R.id.tile_description_tv);
                if (title != null) {
                    title.setText(ei.title);
                }
                if (percentage != null) {
                    percentage.setText(ei.percentage);
                }
                if (description != null) {
                    description.setText(ei.description);
                }
            }
        }
        return v;
    }
}
