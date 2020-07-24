package com.example.fresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Report extends AppCompatActivity {
    private BarChart barChart; //For Chart
    private HorizontalBarChart horchart;
    private DatabaseReference ref; //Firebase
    //Data Model
    double windR=1.1427;double evapR=3.1696;double prcpR=-23.4792;
    double tminR=-0.4665;double tmaxR=0.8526;double taveR=0.1349;
    double npanelsR=0.8838;double yintR=-55.108247;
    //Data Model
    String AWND,EVAP,PRCP,TAVG,TMAX,TMIN;
    Double AWND2,EVAP2,PRCP2,TAVG2,TMAX2,TMIN2;
    Double AWND3Jan=0.0,EVAP3Jan=0.0,PRCP3Jan=0.0,TAVG3Jan=0.0,TMAX3Jan=0.0,TMIN3Jan=0.0,nPanels3Jan=0.0;
    Double AWND3Feb=0.0,EVAP3Feb=0.0,PRCP3Feb=0.0,TAVG3Feb=0.0,TMAX3Feb=0.0,TMIN3Feb=0.0,nPanels3Feb=0.0;
    Double AWND3Mar=0.0,EVAP3Mar=0.0,PRCP3Mar=0.0,TAVG3Mar=0.0,TMAX3Mar=0.0,TMIN3Mar=0.0,nPanels3Mar=0.0;
    Double AWND3Apr=0.0,EVAP3Apr=0.0,PRCP3Apr=0.0,TAVG3Apr=0.0,TMAX3Apr=0.0,TMIN3Apr=0.0,nPanels3Apr=0.0;
    Double AWND3May=0.0,EVAP3May=0.0,PRCP3May=0.0,TAVG3May=0.0,TMAX3May=0.0,TMIN3May=0.0,nPanels3May=0.0;
    Double AWND3Jun=0.0,EVAP3Jun=0.0,PRCP3Jun=0.0,TAVG3Jun=0.0,TMAX3Jun=0.0,TMIN3Jun=0.0,nPanels3Jun=0.0;
    Double AWND3Jul=0.0,EVAP3Jul=0.0,PRCP3Jul=0.0,TAVG3Jul=0.0,TMAX3Jul=0.0,TMIN3Jul=0.0,nPanels3Jul=0.0;
    Double AWND3Aug=0.0,EVAP3Aug=0.0,PRCP3Aug=0.0,TAVG3Aug=0.0,TMAX3Aug=0.0,TMIN3Aug=0.0,nPanels3Aug=0.0;
    Double AWND3Sep=0.0,EVAP3Sep=0.0,PRCP3Sep=0.0,TAVG3Sep=0.0,TMAX3Sep=0.0,TMIN3Sep=0.0,nPanels3Sep=0.0;
    Double AWND3Oct=0.0,EVAP3Oct=0.0,PRCP3Oct=0.0,TAVG3Oct=0.0,TMAX3Oct=0.0,TMIN3Oct=0.0,nPanels3Oct=0.0;
    Double AWND3Nov=0.0,EVAP3Nov=0.0,PRCP3Nov=0.0,TAVG3Nov=0.0,TMAX3Nov=0.0,TMIN3Nov=0.0,nPanels3Nov=0.0;
    Double AWND3Dec=0.0,EVAP3Dec=0.0,PRCP3Dec=0.0,TAVG3Dec=0.0,TMAX3Dec=0.0,TMIN3Dec=0.0,nPanels3Dec=0.0;

    Integer startJan=1,endJan=31,startFeb=32,endFeb=59,
            startMar=60,endMar=90,startApr=91,endApr=120,
            startMay=121,endMay=151,startJun=152,endJun=181,
            startJul=182,endJul=212,startAug=213,endAug=243,
            startSep=244,endSep=273,startOct=274,endOct=304,
            startNov=305,endNov=334,startDec=335,endDec=365;
    //Weather Strings
    Double Feb=0.0,Mar=0.0,Apr=0.0,May=0.0,Jun=0.0,Jul=0.0,Aug=0.0,Sep=0.0,Oct=0.0,Nov=0.0,Dec=0.0;
    //Number of Panels
    Double nPanels1;//Square Footage/Panels Dimensions!!!!!
    Integer nPanels2; //Number of Panels

    //End of Number of Panels


    String zip="Input Zip";
    TextView first;
    TextView annualSavingsBox;
    BottomNavigationView navView;


    TextView carbonEmissionBox;
    TextView ROIBox;
    TextView SavingsBox;

    DecimalFormat form = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        final String nPanels = getIntent().getStringExtra(MainActivity.EXTRA_NUMBER); ///# Panels from MAIN
        nPanels1=Double.parseDouble(nPanels);
        //1 Panel = 354 kWh //Panels Needed
        nPanels2= (int)Math.ceil(nPanels1/354); //Work Cite: Energy Sage


        navView = findViewById(R.id.nav_view);

//Database
        ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Importing Weather Data from Firebase
                for (int i = startJan; i <=endJan; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Jan=AWND3Jan+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Jan=EVAP3Jan+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Jan=PRCP3Jan+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Jan=TAVG3Jan+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Jan=TMAX3Jan+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Jan=TMIN3Jan+TMIN2*tminR;
                    nPanels3Jan=nPanels3Jan+nPanels2*npanelsR;
                }
                Double Jan=(AWND3Jan+EVAP3Jan+PRCP3Jan+TAVG3Jan+TMAX3Jan
                        +TMIN3Jan+nPanels3Jan+yintR*endJan);



                Integer JanInt = Jan.intValue();

                for (int i = startFeb; i <=endFeb; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Feb=AWND3Feb+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Feb=EVAP3Feb+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Feb=PRCP3Feb+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Feb=TAVG3Feb+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Feb=TMAX3Feb+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Feb=TMIN3Feb+TMIN2*tminR;
                    nPanels3Feb=nPanels3Feb+nPanels2*npanelsR;
                }
                Feb=AWND3Feb+EVAP3Feb+PRCP3Feb+TAVG3Feb+TMAX3Feb+TMIN3Feb+nPanels3Feb+yintR*(endFeb-startFeb+1);
                Integer FebInt = Feb.intValue();

                for (int i = startMar; i <=endMar; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Mar=AWND3Mar+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Mar=EVAP3Mar+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Mar=PRCP3Mar+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Mar=TAVG3Mar+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Mar=TMAX3Mar+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Mar=TMIN3Mar+TMIN2*tminR;
                    nPanels3Mar=nPanels3Mar+nPanels2*npanelsR;
                }
                Mar=AWND3Mar+EVAP3Mar+PRCP3Mar+TAVG3Mar+TMAX3Mar+TMIN3Mar+nPanels3Mar+yintR*(endMar-startMar+1);



                Integer MarInt = Mar.intValue();

                for (int i = startApr; i <=endApr; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Apr=AWND3Apr+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Apr=EVAP3Apr+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Apr=PRCP3Apr+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Apr=TAVG3Apr+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Apr=TMAX3Apr+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Apr=TMIN3Apr+TMIN2*tminR;
                    nPanels3Apr=nPanels3Apr+nPanels2*npanelsR;
                }
                Apr=AWND3Apr+EVAP3Apr+PRCP3Apr+TAVG3Apr+TMAX3Apr+TMIN3Apr+nPanels3Apr+yintR*(endApr-startApr+1);
                Integer AprInt = Apr.intValue();

                for (int i = startMay; i <=endMay; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3May=AWND3May+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3May=EVAP3May+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3May=PRCP3May+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3May=TAVG3May+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3May=TMAX3May+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3May=TMIN3May+TMIN2*tminR;
                    nPanels3May=nPanels3May+nPanels2*npanelsR;
                }
                May=AWND3May+EVAP3May+PRCP3May+TAVG3May+TMAX3May+TMIN3May+nPanels3May+yintR*(endMay-startMay+1);



                Integer MayInt = May.intValue();

                for (int i = startJun; i <=endJun; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Jun=AWND3Jun+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Jun=EVAP3Jun+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Jun=PRCP3Jun+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Jun=TAVG3Jun+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Jun=TMAX3Jun+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Jun=TMIN3Jun+TMIN2*tminR;
                    nPanels3Jun=nPanels3Jun+nPanels2*npanelsR;
                }
                Jun=AWND3Jun+EVAP3Jun+PRCP3Jun+TAVG3Jun+TMAX3Jun+TMIN3Jun+nPanels3Jun+yintR*(endJun-startJun+1);
                Integer JunInt = Jun.intValue();

                for (int i = startJul; i <=endJul; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Jul=AWND3Jul+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Jul=EVAP3Jul+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Jul=PRCP3Jul+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Jul=TAVG3Jul+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Jul=TMAX3Jul+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Jul=TMIN3Jul+TMIN2*tminR;
                    nPanels3Jul=nPanels3Jul+nPanels2*npanelsR;
                }
                Jul=AWND3Jul+EVAP3Jul+PRCP3Jul+TAVG3Jul+TMAX3Jul+TMIN3Jul+nPanels3Jul+yintR*(endJul-startJul+1);
                Integer JulInt = Jul.intValue();

                for (int i = startAug; i <=endAug; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Aug=AWND3Aug+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Aug=EVAP3Aug+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Aug=PRCP3Aug+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Aug=TAVG3Aug+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Aug=TMAX3Aug+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Aug=TMIN3Aug+TMIN2*tminR;
                    nPanels3Aug=nPanels3Aug+nPanels2*npanelsR;
                }
                Aug=AWND3Aug+EVAP3Aug+PRCP3Aug+TAVG3Aug+TMAX3Aug+TMIN3Aug+nPanels3Aug+yintR*(endAug-startAug+1);
                Integer AugInt = Aug.intValue();

                for (int i = startSep; i <=endSep; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Sep=AWND3Sep+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Sep=EVAP3Sep+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Sep=PRCP3Sep+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Sep=TAVG3Sep+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Sep=TMAX3Sep+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Sep=TMIN3Sep+TMIN2*tminR;
                    nPanels3Sep=nPanels3Sep+nPanels2*npanelsR;
                }
                Sep=AWND3Sep+EVAP3Sep+PRCP3Sep+TAVG3Sep+TMAX3Sep+TMIN3Sep+nPanels3Sep+yintR*(endSep-startSep+1);
                Integer SepInt = Sep.intValue();

                for (int i = startOct; i <=endOct; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Oct=AWND3Oct+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Oct=EVAP3Oct+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Oct=PRCP3Oct+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Oct=TAVG3Oct+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Oct=TMAX3Oct+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Oct=TMIN3Oct+TMIN2*tminR;
                    nPanels3Oct=nPanels3Oct+nPanels2*npanelsR;
                }
                Oct=AWND3Oct+EVAP3Oct+PRCP3Oct+TAVG3Oct+TMAX3Oct+TMIN3Oct+nPanels3Oct+yintR*(endOct-startOct+1);
                Integer OctInt = Oct.intValue();

                for (int i = startNov; i <=endNov; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Nov=AWND3Nov+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Nov=EVAP3Nov+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Nov=PRCP3Nov+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Nov=TAVG3Nov+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Nov=TMAX3Nov+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Nov=TMIN3Nov+TMIN2*tminR;
                    nPanels3Nov=nPanels3Nov+nPanels2*npanelsR;
                }
                Nov=AWND3Nov+EVAP3Nov+PRCP3Nov+TAVG3Nov+TMAX3Nov+TMIN3Nov+nPanels3Nov+yintR*(endNov-startNov+1);
                Integer NovInt = Nov.intValue();

                for (int i = startDec; i <=endDec; i++) {
                    String a= i+"";
                    AWND = dataSnapshot.child(a).child("AWND").getValue().toString();
                    EVAP = dataSnapshot.child(a).child("EVAP").getValue().toString();
                    PRCP = dataSnapshot.child(a).child("PRCP").getValue().toString();
                    TAVG = dataSnapshot.child(a).child("TAVG").getValue().toString();
                    TMAX = dataSnapshot.child(a).child("TMAX").getValue().toString();
                    TMIN = dataSnapshot.child(a).child("TMIN").getValue().toString();
                    AWND2=Double.parseDouble(AWND);
                    AWND3Dec=AWND3Dec+AWND2*windR;
                    EVAP2=Double.parseDouble(EVAP);
                    EVAP3Dec=EVAP3Dec+EVAP2*evapR;
                    PRCP2=Double.parseDouble(PRCP);
                    PRCP3Dec=PRCP3Dec+PRCP2*prcpR;
                    TAVG2=Double.parseDouble(TAVG);
                    TAVG3Dec=TAVG3Dec+TAVG2*taveR;
                    TMAX2=Double.parseDouble(TMAX);
                    TMAX3Dec=TMAX3Dec+TMAX2*tmaxR;
                    TMIN2=Double.parseDouble(TMIN);
                    TMIN3Dec=TMIN3Dec+TMIN2*tminR;
                    nPanels3Dec=nPanels3Dec+nPanels2*npanelsR;
                }
                Dec=AWND3Dec+EVAP3Dec+PRCP3Dec+TAVG3Dec+TMAX3Dec+TMIN3Dec+nPanels3Dec+yintR*(endDec-startDec+1);
                Integer DecInt = Dec.intValue();
                if( JanInt <0){ JanInt=62;}
                if( FebInt <0){ FebInt=46;}
                if( DecInt <0){ DecInt=75;}

                double annualSavings = Jan+Feb+Mar+Apr+May+Jun+Jul+Aug+Sep+Oct+Nov+Dec;
                annualSavingsBox = findViewById(R.id.annualSavingsResult);
                float b=(float)annualSavings;
                String annualSavingsFormated = form.format(b);
                int panelsforUserROICost=nPanels2;
                panelsforUserROICost= panelsforUserROICost*548;//Cost of Panels: $548 for Each Panels //EnergySage
                //Work Cite https://us.sunpower.com/how-many-solar-panels-do-you-need-panel-size-and-output-factors
                final double SquareFootageforUser = nPanels2*17.55;
                annualSavingsBox.setText("Panels Needed: "+nPanels2+"\n Square Footage Needed: "+form.format(SquareFootageforUser)+"\n Energy Generation: \n"+annualSavingsFormated+" kWh");
                //Work Cite: Carbon Fund (Emission per Electricity) and Carbon Brief (Emission per Solar Energy)
                double carbonEmissionRaw = (annualSavings*0.9884)-(annualSavings*0.0132277);
                String carbonEmission = form.format(carbonEmissionRaw);
                carbonEmissionBox = findViewById(R.id.carbonEmissionResult);
                // Work Cite https://www.epa.gov/energy/greenhouse-gases-equivalencies-calculator-calculations-and-references
                double carbonToTree= carbonEmissionRaw/36.4;
                carbonEmissionBox.setText("Carbon Dioxide (CO2) Saved:\n"+carbonEmission+" LBS"+"\n Trees Needed for CO2 Elimination:"+form.format(carbonToTree));
                first = findViewById(R.id.first);
                zip = getIntent().getStringExtra(MainActivity.EXTRA_TEXT);
                first.setText("For Zip Code: "+zip);
                SavingsBox=findViewById(R.id.test2Result);
                double SRPWinter= (Nov+Dec+Jan+Feb+Mar+Apr)*0.0782;
                double APSWinter= (Nov+Dec+Jan+Feb+Mar+Apr)*0.11;
                double SRPSummer= (May+Jun+Jul+Aug+Sep+Oct)*0.1124;
                double APSSummer = (May+Jun+Jul+Aug+Sep+Oct)*0.13;
                SavingsBox.setText("Winter Cost Nov-Apr: \n SRP: $"+form.format(SRPWinter)+"\nAPS: $"+form.format(APSWinter)+"\n Summer Cost May-Oct: \n SRP: $"+form.format(SRPSummer)+"\nAPS: $"+form.format(APSSummer));
                ROIBox= findViewById(R.id.testResult);
                //Work Cite: Energysage One Panel Cost = $740 $547.6
                double SRPROI=0.0;
                Double ROIYearsSRP=0.0;
                while (panelsforUserROICost>SRPROI){
                    SRPROI= SRPROI+(SRPWinter+SRPSummer);
                    ROIYearsSRP=ROIYearsSRP+1;
                }
                double APSROI=0.0;
                Double ROIYearsAPS=0.0;
                while (panelsforUserROICost>APSROI){
                    APSROI= APSROI+(APSWinter+APSSummer);
                    ROIYearsAPS=ROIYearsAPS+1;
                }
                ROIBox.setText("Investment Cost: $"+panelsforUserROICost+"\n ROI based on SRP: "+ROIYearsSRP+" Years"+"\n ROI based on APS: "+ROIYearsAPS+" Years");

                //On data Change Continued
                //Start Chart
                barChart = (BarChart) findViewById(R.id.chart1);
                barChart.getDescription().setEnabled(false);
                barChart.setFitBars(true);
                ArrayList<BarEntry> yVals = new ArrayList<>();
                yVals.add(new BarEntry(1,JanInt));
                yVals.add(new BarEntry(2,FebInt));
                yVals.add(new BarEntry(3,MarInt));
                yVals.add(new BarEntry(4,AprInt));
                yVals.add(new BarEntry(5,MayInt));
                yVals.add(new BarEntry(6,JunInt));
                yVals.add(new BarEntry(7,JulInt));
                yVals.add(new BarEntry(8,AugInt));
                yVals.add(new BarEntry(9,SepInt));
                yVals.add(new BarEntry(10,OctInt));
                yVals.add(new BarEntry(11,NovInt));
                yVals.add(new BarEntry(12,DecInt));

                BarDataSet set = new BarDataSet(yVals, "Monthly Solar Energy in kWh");
                set.setColors(ColorTemplate.MATERIAL_COLORS);
                set.setDrawValues(true);

                BarData data = new BarData(set);

                barChart.setData(data);
                barChart.invalidate();
                barChart.animateY(500);


                //Horizontal Chart
                /*
                horchart = (HorizontalBarChart) findViewById(R.id.chart1);

                ArrayList<BarEntry> yVals = new ArrayList<>();
                float spaceForBar =10f;
                yVals.add(new BarEntry(0,100));
                yVals.add(new BarEntry(1*spaceForBar,120));
                BarDataSet set1;
                BarDataSet set2;
                set1=new BarDataSet(yVals,"Data Set1");
                set1=new BarDataSet(yVals,"Data Set2");
                BarData data = new BarData(set1);
                BarData data2 = new BarData(set2);

                //BarDataSet set = new BarDataSet(yVals, "Monthly Solar Energy in kWh");
                //BarData data = new BarData(set);

                horchart.setData(data);

//End Hori Chart

                 */

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //update UI here if error occurred.

            }

        });

        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }//End of On Create

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override

                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            Intent mainIntent = new Intent(Report.this, MainActivity.class);
                            startActivity(mainIntent);
                            break;
                        case R.id.report:
                            break;
                        case R.id.info:
                            Intent info = new Intent(Report.this, Info.class);
                            startActivity(info);
                            break;

                    }
                    return false;
                }
            };
}