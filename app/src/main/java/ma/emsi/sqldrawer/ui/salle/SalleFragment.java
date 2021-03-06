package ma.emsi.sqldrawer.ui.salle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ma.emsi.sqldrawer.R;
import ma.emsi.sqldrawer.adapter.MySalleRecyclerViewAdapter;
import ma.emsi.sqldrawer.beans.Machine;
import ma.emsi.sqldrawer.beans.Salle;
import ma.emsi.sqldrawer.dao.IDao;
import ma.emsi.sqldrawer.service.MachineService;
import ma.emsi.sqldrawer.service.SalleService;
import ma.emsi.sqldrawer.ui.salle.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class SalleFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SalleFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SalleFragment newInstance(int columnCount) {
        SalleFragment fragment = new SalleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }
    SalleService ss;
    MachineService ms;
    RecyclerView recyclerView;
    MySalleRecyclerViewAdapter adapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int id = Integer.parseInt( ((TextView)viewHolder.itemView.findViewById(R.id.idS)).getText().toString());
            Salle salle = ss.findById(id);
            List<Machine> machines = ms.findMachines(id);
            for(Machine machine : machines){
                ms.delete(machine);
            }
            ss.delete(salle);
            adapter = new MySalleRecyclerViewAdapter(ss.findAll(), getFragmentManager());
            recyclerView.setAdapter(adapter);
            Toast.makeText(getContext(), "Salle supprimer!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ss = new SalleService(getActivity());


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salle_item_list, container, false);
        ss = new SalleService(getActivity());
        ms = new MachineService(getActivity());

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MySalleRecyclerViewAdapter(ss.findAll(),getFragmentManager());
            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);

        }
        return view;
    }
}