package com.sofiane.envol.ownersquarehangout.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.sofiane.envol.ownersquarehangout.Activities.DetailBookingActivity;
import com.sofiane.envol.ownersquarehangout.Adapters.SearchViewAdapter;
import com.sofiane.envol.ownersquarehangout.Entities.Booking;
import com.sofiane.envol.ownersquarehangout.R;
import com.sofiane.envol.ownersquarehangout.Utilies.Section;
import com.sofiane.envol.ownersquarehangout.Utilies.StatelessSection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchActivityFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchViewAdapter sectionAdapter;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();

    private RecyclerView recyclerView;
    private List<String> contacts;
    public String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search2, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        sectionAdapter = new SearchViewAdapter();
        data();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        names.clear();
        contacts.clear();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle("search");

        }


    }

    private void data() {

        SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final String id_rest = pref.getString("login_shared", "def");
        Firebase fire = new Firebase("https://hangoutenvol.firebaseio.com/Booking/");
        final Calendar now = Calendar.getInstance();


        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //     Toast.makeText(getActivity(), "" + "There are " + snapshot.getChildrenCount() + " blog posts", Toast.LENGTH_SHORT).show();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Booking post = postSnapshot.getValue(Booking.class);
                    if (post.getIdPlace().equals(id_rest) && post.getDate_book().equals("" + now.get(Calendar.DAY_OF_MONTH) + "/" +
                            (now.get(Calendar.MONTH) + 1) + "/" +
                            now.get(Calendar.YEAR))) {
                        names.add(post.getName_user()+"~#"+post.getIdBooking());
                        ids.add(post.getIdBooking());
                        sectionAdapter.notifyDataSetChanged();

                    }

                }

                for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {


                    contacts = getContactsWithLetter(alphabet, names);
                    if (contacts.size() > 0) {
                        ContactsSection contactsSection = new ContactsSection(String.valueOf(alphabet), contacts);
                        sectionAdapter.addSection(contactsSection);
                    }
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_name, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextChange(String query) {


        for (Section section : sectionAdapter.getSectionsMap().values()) {
            if (section instanceof FilterableSection) {
                ((FilterableSection) section).filter(query);
            }
        }
        sectionAdapter.notifyDataSetChanged();

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<String> getContactsWithLetter(char letter, ArrayList<String> names) {
        List<String> contacts = new ArrayList<>();
        contacts.clear();
        for (String contact : names) {

            String[] parts = contact.split("~#");

            final String part1 = parts[0];
            String part2 = parts[1];

            if (part1.charAt(0) == letter) {
                contacts.add(contact);
            }
        }

        return contacts;
    }


    class ContactsSection extends StatelessSection implements FilterableSection {

        String title;
        List<String> list;
        List<String> filteredList;

        public ContactsSection(String title, List<String> list) {
            super(R.layout.section_header, R.layout.section_item);

            this.title = title;
            this.list = list;
            this.filteredList = new ArrayList<>(list);

        }


        @Override
        public int getContentItemsTotal() {
            return filteredList.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {

            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            SharedPreferences pref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            final String id_rest = pref.getString("login_shared", "def");

            final String name = filteredList.get(position);


            Firebase fire = new Firebase("https://hangoutenvol.firebaseio.com/Booking");
            String[] parts = name.split("~#");
            final String part1 = parts[0];
            String part2 = parts[1];

            Query d = fire.orderByChild("idBooking").equalTo(part2);
            d.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    for (final DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Booking post = postSnapshot.getValue(Booking.class);

                        if (post.getIdPlace().equals(id_rest)) {
                            itemHolder.tvItem.setText(part1);
                            if (post.getInfo().equals("validate")) {
                                itemHolder.tvItem.setTextColor(getResources().getColor(R.color.red));
                                itemHolder.tvItem.setTypeface(null, Typeface.BOLD);
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (itemHolder.tvItem.getCurrentTextColor() != getResources().getColor(R.color.red)) {
                        Intent intent = new Intent(getActivity(), DetailBookingActivity.class);
                        intent.putExtra("position", filteredList.get(sectionAdapter.getSectionPosition(itemHolder.getAdapterPosition())));
                        intent.putExtra("id", ids.get(sectionAdapter.getSectionPosition(itemHolder.getAdapterPosition())));
                        onPause();
                        getActivity().startActivity(intent);
                    }

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }

        @Override
        public void filter(String query) {
            if (TextUtils.isEmpty(query)) {
                filteredList = new ArrayList<>(list);
                this.setVisible(true);
            } else {
                filteredList.clear();
                for (String value : list) {
                    if (value.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(value);
                    }
                }

                this.setVisible(!filteredList.isEmpty());
            }
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;

        public HeaderViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final ImageView imgItem;
        private final TextView tvItem;

        public ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = (ImageView) view.findViewById(R.id.imgItem);
            tvItem = (TextView) view.findViewById(R.id.tvItem);
        }
    }

    interface FilterableSection {
        void filter(String query);
    }
}
