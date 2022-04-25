package ma.emsi.sqldrawer.ui.stats;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import ma.emsi.sqldrawer.R;
import ma.emsi.sqldrawer.beans.Machine;
import ma.emsi.sqldrawer.beans.Salle;
import ma.emsi.sqldrawer.service.MachineService;
import ma.emsi.sqldrawer.service.SalleService;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class statsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        BarChart barChart = v.findViewById(R.id.nbM);
        SalleService ss = new SalleService(getContext());
        MachineService ms = new MachineService(getContext());
        int indice=0, nbrMachine=0;

        ArrayList<BarEntry> machines = new ArrayList<>();
        ArrayList<String> salles = new ArrayList<>();

        for(Salle salle : ss.findAll()){
            for(Machine machine : ms.findMachines(salle.getId())){

                nbrMachine++;


            }
            machines.add(new BarEntry(indice,nbrMachine));
            salles.add(salle.getCode());

            indice++;
            nbrMachine=0;
        }

        BarDataSet barDataSet = new BarDataSet(machines,"nbr Machine");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        BarData barData = new BarData(barDataSet);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(salles));
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(270);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Nombre machine par salle");
        barChart.animateY(2000);
        barChart.invalidate();
        return v;

    }
}