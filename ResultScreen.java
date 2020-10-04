package com.re.developers.vjtispi.calculatornew;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.PointerIconCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ResultScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    int count = 0;
    float[] credits = new float[15];
    int id = 1000;
    Integer[] items = {10, 9, 8, 7, 6, 5, 4, 2, 0};
    private AdView mAdView;
    DatabaseHelper myDB;
    float[] pointers = new float[15];
    float spi = 10.0f;
    TextView spiText;
    float sumcredits = 0.0f;
    float summarks = 0.0f;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.result_screen_layout);
        this.spiText = (TextView) findViewById(R.id.textViewSPI);
        MobileAds.initialize(this, "ca-app-pub-3998047397697270~5005892902");
        this.mAdView = (AdView) findViewById(R.id.adView);
        this.mAdView.loadAd(new AdRequest.Builder().build());
        ((Button) findViewById(R.id.backbutton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ResultScreen.this.onBackPressed();
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        final int id1 = Integer.parseInt(getIntent().getStringExtra("Message"));
        int idForSQL = id1;
        ((Button) findViewById(R.id.electivebutton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ResultScreen.this.DisplayElectiveList(id1);
            }
        });
        if (id1 / 1000 == 2) {
            if (id1 % 10 == 2) {
                idForSQL = 2002;
            }
            if (id1 % 10 == 3) {
                idForSQL = 2003;
            }
        } else if (id1 / 1000 == 1) {
            int idBranch = (id1 / 10) % 100;
            if (id1 % 10 == 0) {
                if (idBranch > 4) {
                    idForSQL = 1070;
                } else {
                    idForSQL = 1000;
                }
            }
            if (id1 % 10 == 1) {
                if (idBranch > 4) {
                    idForSQL = 1071;
                } else {
                    idForSQL = PointerIconCompat.TYPE_CONTEXT_MENU;
                }
            }
        }
        this.myDB = new DatabaseHelper(this);
        if (this.myDB.getDetailsOfStudentCode(idForSQL).getCount() == 0) {
            createDatabase(idForSQL);
        }
        Cursor res = this.myDB.getDetailsOfStudentCode(idForSQL);
        int tag = 0;
        if (res.getCount() == 0) {
            Toast.makeText(this, "No data", 1).show();
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("ID " + res.getInt(0) + "\n");
                buffer.append("Program " + res.getString(1) + "\n");
                buffer.append("Branch " + res.getString(2) + "\n");
                buffer.append("Semester " + res.getString(3) + "\n");
                buffer.append("Student Code " + res.getInt(4) + "\n");
                buffer.append("Subject Code " + res.getInt(5) + "\n");
                buffer.append("Subject Name " + res.getString(6) + "\n");
                buffer.append("Credits " + res.getFloat(7) + "\n");
                buffer.append("\n");
                View view = getLayoutInflater().inflate(R.layout.subject_layout, linearLayout, false);
                ((TextView) view.findViewById(R.id.textViewSubject)).setText("" + res.getString(6));
                ((TextView) view.findViewById(R.id.textViewCredits)).setText("" + res.getString(7));
                Spinner pointerSpinner = (Spinner) view.findViewById(R.id.spinnerPointer);
                pointerSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, this.items));
                this.count++;
                this.sumcredits += res.getFloat(7);
                this.credits[tag] = res.getFloat(7);
                pointerSpinner.setTag("" + tag);
                tag++;
                pointerSpinner.setOnItemSelectedListener(this);
                linearLayout.addView(view);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle((CharSequence) "DATA");
            builder.setMessage((CharSequence) buffer);
        }
        int id2 = (id1 / 10) % 100;
        String StringProgram = "";
        String StringBranch = "";
        String StringSemester = "";
        switch (id1 % 10) {
            case 0:
                StringSemester = "I";
                break;
            case 1:
                StringSemester = "II";
                break;
            case 2:
                StringSemester = "III";
                break;
            case 3:
                StringSemester = "IV";
                break;
            case 4:
                StringSemester = "V";
                break;
            case 5:
                StringSemester = "VI";
                break;
            case 6:
                StringSemester = "VII";
                break;
            case 7:
                StringSemester = "VIII";
                break;
        }
        switch (id1 / 1000) {
            case 1:
                StringProgram = "B.TECH";
                switch (id2) {
                    case 0:
                        StringBranch = "CIVIL";
                        break;
                    case 1:
                        StringBranch = "MECHANICAL";
                        break;
                    case 2:
                        StringBranch = "ELECTRICAL";
                        break;
                    case 3:
                        StringBranch = "PRODUCTION";
                        break;
                    case 4:
                        StringBranch = "TEXTILE";
                        break;
                    case 5:
                        StringBranch = "ELECTRONICS";
                        break;
                    case 6:
                        StringBranch = "COMPUTER";
                        break;
                    case 7:
                        StringBranch = "IT";
                        break;
                    case 8:
                        StringBranch = "EXTC";
                        break;
                }
            case 2:
                StringProgram = "M.Tech";
                switch (id2) {
                    case 0:
                        StringBranch = "AUTOMOBILE";
                        break;
                    case 1:
                        StringBranch = "CAD-CAM";
                        break;
                    case 2:
                        StringBranch = "MACHINE DESIGN";
                        break;
                    case 3:
                        StringBranch = "THERMAL ENGINEERING";
                        break;
                    case 4:
                        StringBranch = "CONSTRUCTION MANAGEMENT";
                        break;
                    case 5:
                        StringBranch = "ENVIRONMENTAL";
                        break;
                    case 6:
                        StringBranch = "STRUCTURAL ENGINEERING";
                        break;
                    case 7:
                        StringBranch = "CONTROL SYSTEMS";
                        break;
                    case 8:
                        StringBranch = "POWER SYSTEMS";
                        break;
                    case 9:
                        StringBranch = "ELECTRONICS";
                        break;
                    case 10:
                        StringBranch = "EXTC";
                        break;
                    case 11:
                        StringBranch = "NIMS";
                        break;
                    case 12:
                        StringBranch = "COMPUTER";
                        break;
                    case 13:
                        StringBranch = "SOFTWARE";
                        break;
                    case 14:
                        StringBranch = "PRODUCTION";
                        break;
                    case 15:
                        StringBranch = "PROJECT MANAGEMENT";
                        break;
                    case 16:
                        StringBranch = "TEXTILE TECHNOLOGY";
                        break;
                }
            case 3:
                StringProgram = "MCA";
                break;
        }
        getSupportActionBar().setTitle((CharSequence) "" + StringProgram + " " + StringBranch + " " + StringSemester);
    }

    public void DisplayElectiveList(int id2) {
        StringBuffer buffer = new StringBuffer();
        int id22 = (id2 / 10) % 100;
        switch (id2 / 1000) {
            case 1:
                switch (id2 % 10) {
                    case 5:
                        switch (id22) {
                            case 0:
                                buffer.append("Elective I \n");
                                buffer.append("\nGeographic Information System");
                                buffer.append("\nAdvanced Hydrology");
                                buffer.append("\nAdvanced Structural Analysis");
                                buffer.append("\nAdvanced Foundation Engineering");
                                break;
                            case 1:
                                buffer.append("Elective I \n");
                                buffer.append("\nComputational Fluid Dynamics");
                                buffer.append("\nRobotics");
                                buffer.append("\nWelding Technology");
                                break;
                            case 2:
                                buffer.append("Elective I \n");
                                buffer.append("\nIntroduction to Renewable Energy");
                                buffer.append("\nOOPs in Power System");
                                buffer.append("\nIndustrial Automation");
                                break;
                            case 3:
                                buffer.append("Elective I \n");
                                buffer.append("\nOperation Management");
                                buffer.append("\nProduct Life Cycle Management");
                                buffer.append("\nTechnology Management");
                                buffer.append("\nDynamics of Machinery");
                                buffer.append("\nArtificial Intelligence and Expert Systems");
                                buffer.append("\nElective II \n");
                                buffer.append("\nUnconventional and Micro Machining Processes");
                                buffer.append("\nRefrigeration and Air Conditioning");
                                buffer.append("\nBusiness Analytics");
                                buffer.append("\nMaterial Handling and Storage");
                                buffer.append("\nMIS and ERP");
                                break;
                            case 4:
                                buffer.append("Elective I \n");
                                buffer.append("\nPost Spinning Operation of Man-Made Fibres");
                                buffer.append("\nAdvance Garment Designing and Quality Control");
                                break;
                            case 5:
                                buffer.append("Elective I \n");
                                buffer.append("\nImage and Video Processing");
                                buffer.append("\nNeural Network and Fuzzy Logic");
                                buffer.append("\nAudio, Video and Image Compression");
                                buffer.append("\nVirtual Instrumentation");
                                break;
                            case 6:
                                buffer.append("Elective I \n");
                                buffer.append("\nWireless Networking");
                                buffer.append("\nGraph Theory and Application");
                                buffer.append("\nLinux Internals");
                                break;
                            case 7:
                                buffer.append("Elective I \n");
                                buffer.append("\nBio-Informatics");
                                buffer.append("\nCompiler Design");
                                buffer.append("\nGraph Theory");
                                break;
                            case 8:
                                buffer.append("Elective I \n");
                                buffer.append("\nTelecom Network Management");
                                buffer.append("\nVoIP Technology");
                                buffer.append("\nAdvanced Digital Signal Processing");
                                break;
                        }
                    case 6:
                        switch (id22) {
                            case 0:
                                buffer.append("Elective II \n");
                                buffer.append("\nTerrain Data Analysis");
                                buffer.append("\nEnvironmental Management");
                                buffer.append("\nWater Resources System Planning and Management");
                                buffer.append("\nRehabilitation of Buildings");
                                buffer.append("\nAdvanced Construction Techniques");
                                buffer.append("\nIntroduction to Non linear Analysis");
                                buffer.append("\nSoil Dynamics and Machine Foundation");
                                buffer.append("\n\nOpen Elective\n");
                                buffer.append("\nSustainable Development");
                                buffer.append("\nRisk and Value Management");
                                break;
                            case 1:
                                buffer.append("Elective II \n");
                                buffer.append("\nFinite Element Method");
                                buffer.append("\nTransport Phenomenon");
                                buffer.append("\nMolecular Dynamics");
                                buffer.append("\n\nOpen Elective\n");
                                buffer.append("\nEnergy Conservation and Management");
                                buffer.append("\nNanotechnology");
                                buffer.append("\nTotal Quality Management");
                                break;
                            case 2:
                                buffer.append("Elective II \n");
                                buffer.append("\nBiomedical Electronics");
                                buffer.append("\nFACTS");
                                buffer.append("\nSubstation Automation, PLC and SCADA");
                                break;
                            case 3:
                                buffer.append("\nNo Elective Subjects for this semester.");
                                break;
                            case 4:
                                buffer.append("Elective II \n");
                                buffer.append("\nAdvanced Finishing");
                                buffer.append("\nFashion Designing");
                                buffer.append("\nHigh Tech Fibres");
                                buffer.append("\nProduction and Performance of Home Textiles");
                                buffer.append("\n\nOpen Elective \n");
                                buffer.append("\nTextiles in Field of Engineering Applications");
                                break;
                            case 5:
                                buffer.append("Elective II \n");
                                buffer.append("\nBiomedical Instrumentation");
                                buffer.append("\nError Correcting Codes");
                                buffer.append("\nSpeech Processing");
                                buffer.append("\n\nOpen Elective \n");
                                buffer.append("\nSignal Processing and Applications");
                                break;
                            case 6:
                                buffer.append("Elective II \n");
                                buffer.append("\nPervasive Computing");
                                buffer.append("\nDigital Image Processing");
                                buffer.append("\nSoftware Testing and Quality Assurance");
                                buffer.append("\nInformation Storage and Management");
                                buffer.append("\nBio-Informatics");
                                buffer.append("\nSoftware Project Management");
                                buffer.append("\n\nOpen Elective \n");
                                buffer.append("\nIntellectual Property Rights");
                                buffer.append("\nData Structure");
                                buffer.append("\nDatabase Management Systems");
                                buffer.append("\nSoftware Engineering");
                                break;
                            case 7:
                                buffer.append("Elective II \n");
                                buffer.append("\nAdvance Database Management System");
                                buffer.append("\nDigital Image Processing");
                                buffer.append("\nNetwork Security");
                                buffer.append("\nSystem Administration");
                                buffer.append("\nMultimedia Systems");
                                buffer.append("\nSoftware Testing");
                                buffer.append("\n\nOpen Elective \n");
                                buffer.append("\nInformation Security and Digital Forensics");
                                buffer.append("\nData Communication and Networking");
                                buffer.append("\nWeb Technology");
                                buffer.append("\nData Structure");
                                break;
                            case 8:
                                buffer.append("Elective II \n");
                                buffer.append("\nComputer Organization");
                                buffer.append("\nPattern Recognition");
                                buffer.append("\nVirual Instrumentation");
                                buffer.append("\n\nOpen Elective \n");
                                buffer.append("\nEffect of EMI and EMC");
                                break;
                        }
                    case 7:
                        switch (id22) {
                            case 0:
                                buffer.append("Elective III \n");
                                buffer.append("\nSolid and Hazardous Waste Management");
                                buffer.append("\nWatershade Management");
                                buffer.append("\nPavement Management System");
                                buffer.append("\nPavement Design and Construction");
                                buffer.append("\nDesign of Prestressed Concrete Structures");
                                buffer.append("\nGeo Synthetic Engineering");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nUnit Operations and Processes in Environmental Engineering");
                                buffer.append("\nTransportation Planning and Traffic Engineering");
                                buffer.append("\nProject Appraisal");
                                buffer.append("\nEnvironmental Impact Assessment");
                                buffer.append("\nIntroduction to Finite Element");
                                buffer.append("\nAdvanced Design of Steel Structures");
                                break;
                            case 1:
                                buffer.append("Elective III \n");
                                buffer.append("\nAutomobile Engineering");
                                buffer.append("\nComposite Materials");
                                buffer.append("\nRapid Product Development");
                                buffer.append("\nDesign of Experiments");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nTribology");
                                buffer.append("\nSystem Modelling and Analysis");
                                buffer.append("\nMicro Electro-Mechanical Systems");
                                buffer.append("\nSupply Chain Management");
                                buffer.append("\nPressure Vessel Design");
                                buffer.append("\nPiping Engineering");
                                buffer.append("\nCryogenic Engineering");
                                buffer.append("\nProject Management");
                                buffer.append("\nFailure Analysis and Design");
                                buffer.append("\nGas Dynamics and Propulsion");
                                break;
                            case 2:
                                buffer.append("Elective III \n");
                                buffer.append("\nRobotics and Automation");
                                buffer.append("\nMicro Grids");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nPower System Planning");
                                buffer.append("\nRestructured Power System");
                                buffer.append("\nAppliec Linear Algebra");
                                break;
                            case 3:
                                buffer.append("Elective I \n");
                                buffer.append("\nSales and Marketing");
                                buffer.append("\nAdvanced Machine Design");
                                buffer.append("\nComputational Fluid Dynamics");
                                buffer.append("\nTotal Quality Management");
                                buffer.append("\nEnergy Management");
                                buffer.append("\nPlastic Engineering");
                                buffer.append("\nOperation Strategy");
                                buffer.append("\nFlexible Manufacturing Systems");
                                buffer.append("\nFinite Element Method for Manufacturing");
                                buffer.append("\nMicro and Nano manufacturing System");
                                buffer.append("\nNanomodelling and Applications: Molecular Simulations");
                                buffer.append("\nBusiness Analytics");
                                break;
                            case 4:
                                buffer.append("Elective III \n");
                                buffer.append("\nCoated and Laminated Textiles");
                                buffer.append("\nCAD/CAM Applications for Clothing");
                                buffer.append("\nAdvances in Textile Testing");
                                buffer.append("\nGreen Composites");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nRetail and Supply Chain Management");
                                buffer.append("\nNano Technology in Textiles");
                                buffer.append("\nFunctional and Smart Textiles");
                                buffer.append("\nProject formulation and Appraisal in Textile Sector");
                                break;
                            case 5:
                                buffer.append("Elective III \n");
                                buffer.append("\nProcess Control Instrumentation");
                                buffer.append("\nMicrocomputer System Design");
                                buffer.append("\nDSP Processors");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nSatellite Communication");
                                buffer.append("\nMechatronics");
                                buffer.append("\nNext Generation Networks");
                                buffer.append("\nWireless Sensor Network");
                                buffer.append("\nE-Security");
                                break;
                            case 6:
                                buffer.append("Elective III \n");
                                buffer.append("\nMultimedia Systems");
                                buffer.append("\nManagement Information System");
                                buffer.append("\nSensor Networks");
                                buffer.append("\nDigital Forensic Analysis");
                                buffer.append("\nComputational Biology");
                                buffer.append("\nDistributed Algorithms");
                                buffer.append("\nSoft Computing");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nMulticore Technologies");
                                buffer.append("\nSystem Administration");
                                buffer.append("\nSoftware Design Patterns");
                                buffer.append("\nVirtual Reality");
                                break;
                            case 7:
                                buffer.append("Elective III \n");
                                buffer.append("\nDigital Forensics");
                                buffer.append("\nSoft Computing Techniques");
                                buffer.append("\nNumber Theory");
                                buffer.append("\nComputational Biology");
                                buffer.append("\nE-Commerce Systems");
                                buffer.append("\nInternet of Things");
                                buffer.append("\nDistributed Algorithms");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nSensor Networks");
                                buffer.append("\nMulticore Technologies");
                                buffer.append("\nVirtual Reality");
                                break;
                            case 8:
                                buffer.append("Elective III \n");
                                buffer.append("\nMultimedia Communication");
                                buffer.append("\nData Compression and Encryption");
                                buffer.append("\nWireless Sensor Network");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nDigital Image Processing");
                                buffer.append("\nCognitive Radio");
                                buffer.append("\nNext Generation Network");
                                buffer.append("\nInternet Security");
                                break;
                        }
                    default:
                        buffer.append("No Elective subjects for this Semester.");
                        break;
                }
            case 2:
                switch (id2 % 10) {
                    case 0:
                        switch (id22) {
                            case 0:
                                buffer.append("Elective I \n");
                                buffer.append("\nSystem Modelling and Analysis");
                                buffer.append("\nEnergy Conservation and Management");
                                buffer.append("\nReliability Engineering");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nVehicle Performance");
                                buffer.append("\nFinite Element Methods");
                                break;
                            case 1:
                                buffer.append("Elective I \n");
                                buffer.append("\nSystem Modelling and Analysis");
                                buffer.append("\nEnergy Conservation and Management");
                                buffer.append("\nReliability Engineering");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nFluid Power Automation");
                                buffer.append("\nRapid Product Development");
                                buffer.append("\nOperations Management");
                                break;
                            case 2:
                                buffer.append("Elective I \n");
                                buffer.append("\nSystem Modelling and Analysis");
                                buffer.append("\nEnergy Conservation and Management");
                                buffer.append("\nReliability Engineering");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nFluid Power Automation");
                                buffer.append("\nRapid Product Development");
                                buffer.append("\nOperations Management");
                                break;
                            case 3:
                                buffer.append("Elective I \n");
                                buffer.append("\nSystem Modelling and Analysis");
                                buffer.append("\nEnergy Conservation and Management");
                                buffer.append("\nReliability Engineering");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nAdvanced Refrigeration and Air Conditioning");
                                buffer.append("\nAdvanced Internal Combustion Engines");
                                break;
                            case 4:
                                buffer.append("Elective I \n");
                                buffer.append("\nRisk and Value Management");
                                buffer.append("\nManagerial Decision Making");
                                buffer.append("\nEIA and Audit");
                                buffer.append("\nEnvironmental Management");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nRehabitilation of Structures");
                                buffer.append("\nEnergy Conservation in Facility Design and Construction");
                                buffer.append("\nOperational Health and Safety Management");
                                buffer.append("\nConstruction Entrepreneurship");
                                break;
                            case 5:
                                buffer.append("Elective I \n");
                                buffer.append("\nRisk and Value Management");
                                buffer.append("\nManagerial Decision Making");
                                buffer.append("\nEIA and Audit");
                                buffer.append("\nEnvironmental Management");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nAir and Noise Pollution and Control");
                                buffer.append("\nGroundwater Hydrology and Contamination");
                                buffer.append("\nEnergy Conservation in Facility Design and Construction");
                                buffer.append("\nInternal Construction Business");
                                break;
                            case 6:
                                buffer.append("Elective I \n");
                                buffer.append("\nNon Linear Analysis");
                                buffer.append("\nMechanics of Composite Materials");
                                buffer.append("\nRepairs and Rehabitilations of Structures");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nAdvanced Structural Mechanics");
                                buffer.append("\nDesign of Offshore Structrures");
                                break;
                            case 7:
                                buffer.append("Elective I \n");
                                buffer.append("\nRobotics: Dynamics and Control");
                                buffer.append("\nstochastic Control");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nDecentralized Control ");
                                buffer.append("\nMathematical System Theory");
                                break;
                            case 8:
                                buffer.append("Elective I \n");
                                buffer.append("\nSmart Grid and Development");
                                buffer.append("\nPower Quality");
                                buffer.append("\nPower Electronics and FACTS devices");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nRenewable Energy Systems");
                                buffer.append("\nHigh Voltage Engineering");
                                buffer.append("\nDigital Signal Processing");
                                break;
                            case 9:
                                buffer.append("Elective I \n");
                                buffer.append("\nElectronics in Medicine");
                                buffer.append("\nArificial Neural Networks and Machine Learning");
                                buffer.append("\nE-Security");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nApplications of DSP");
                                buffer.append("\nModern Communication Networks");
                                buffer.append("\nRF Integrated Circuits");
                                break;
                            case 10:
                                buffer.append("Elective I \n");
                                buffer.append("\nNext Generation Networks");
                                buffer.append("\nArtificial Neural Networks and Machine Learning");
                                buffer.append("\nE-Security");
                                buffer.append("\n\nElective II \n");
                                buffer.append("\nApplications of DSP");
                                buffer.append("\nOptical Communication");
                                buffer.append("\nData Compression");
                                break;
                            case 11:
                                buffer.append("Elective I\n");
                                buffer.append("\nComputer Systems Performance Analysis");
                                buffer.append("\nDistributed Systems");
                                buffer.append("\nWireless Networks and Mobile Computing");
                                buffer.append("\n\nElective II\n");
                                buffer.append("\nMultimedia Processing Systems");
                                buffer.append("\nAlgorithms and Complexity");
                                buffer.append("\nSemantic web and Social Networks");
                                buffer.append("\nData Center Management");
                                break;
                            case 12:
                                buffer.append("Elective I\n");
                                buffer.append("\nDistributed Systems");
                                buffer.append("\nDesign of Parallel Architecture and Programming");
                                buffer.append("\nWireless Networks and Mobile Computing ");
                                buffer.append("\nComputer Systems Performance Analysis");
                                buffer.append("\n\nElective II\n");
                                buffer.append("\nMultimedia Processing Systems");
                                buffer.append("\nAlgorithms and Complexity");
                                buffer.append("\nSemantic Web and Social Networks");
                                buffer.append("\nWeb Personalization and Optimization");
                                break;
                            case 13:
                                buffer.append("Elective I\n");
                                buffer.append("\nDistributed Systems");
                                buffer.append("\nData Mining");
                                buffer.append("\nMobile Communication and Computing");
                                buffer.append("\nComputer Systems Performance Analysis");
                                buffer.append("\n\nElective II\n");
                                buffer.append("\nCloud Architecture, Infrastructure and Technology");
                                buffer.append("\nHuman Computer Interaction");
                                buffer.append("\nSemantic Web and Social Networks");
                                buffer.append("\nWeb Personalization and Optimization");
                                break;
                            case 16:
                                buffer.append("Elective I\n");
                                buffer.append("\nAdvanced Textile Materials");
                                buffer.append("\nCosting, Project Formulation and Appraisal");
                                buffer.append("\n\nElective II\n");
                                buffer.append("\nApparel Engineering and Quality Control");
                                buffer.append("\nCoated and Laminated Textiles");
                                break;
                        }
                    case 1:
                        switch (id22) {
                            case 0:
                                buffer.append("Elective III \n");
                                buffer.append("\nAutomotive Materials");
                                buffer.append("\nCondition Monitoring");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nComputational Fluid Dynamics");
                                buffer.append("\nAutomotive Electronics");
                                buffer.append("\nRobotics");
                                break;
                            case 1:
                                buffer.append("Elective III \n");
                                buffer.append("\nPressure Vessel Design");
                                buffer.append("\nProcess Equipment Design");
                                buffer.append("\nDesign of Material handling Equipment");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nAdvanced Machine Design");
                                buffer.append("\nComputational Fluid Dynamics");
                                break;
                            case 2:
                                buffer.append("Elective III \n");
                                buffer.append("\nPressure Vessel Design");
                                buffer.append("\nProcess Equipment Design");
                                buffer.append("\nDesign of Material handling Equipment");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nAdvanced Machine Design");
                                buffer.append("\nComputational Fluid Dynamics");
                                break;
                            case 3:
                                buffer.append("Elective III \n");
                                buffer.append("\nCryogenics");
                                buffer.append("\nGas Turbine and Propulsion");
                                buffer.append("\nProcess Equipment Design");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nEnenrgy Conversion Systems");
                                buffer.append("\nFinite Element Methods");
                                buffer.append("\nAdvanced Turbo Machinery");
                                break;
                            case 4:
                                buffer.append("Elective III \n");
                                buffer.append("\nIntegrated GIS & GPS in Infrastructures");
                                buffer.append("\nWater Resource Management");
                                buffer.append("\nConstruction Management information systems");
                                buffer.append("\nFacilities Management");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nQuality Assurance on Construction Projects");
                                buffer.append("\nPavement Management Systems");
                                buffer.append("\nSustainable Building Construction");
                                buffer.append("\nInternational Construction Business");
                                break;
                            case 5:
                                buffer.append("Elective III \n");
                                buffer.append("\nIndustrial Wastewater Treatment");
                                buffer.append("\nOperation and Maintenance of Treatment Facilities");
                                buffer.append("\nOperational Health and Safety Management");
                                buffer.append("\nQaulity Assurance on Construction Projects");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nEnvironmental Legislation");
                                buffer.append("\nRural water supply and Sanitation");
                                buffer.append("\nIntegrated GIS & GPS in infrastructure");
                                buffer.append("\nWater Resource Management");
                                break;
                            case 6:
                                buffer.append("Elective III \n");
                                buffer.append("\nStructutral Dynamics and Earthquake Engineering");
                                buffer.append("\nBridge Engineering");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nMechanics of Plates and Shells");
                                buffer.append("\nEarth Pressure and Retaining Structures");
                                buffer.append("\nDesign of Tall Structure");
                                break;
                            case 7:
                                buffer.append("Elective III \n");
                                buffer.append("\nMultivariable Control");
                                buffer.append("\nHigh Performance Electric Drives");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nIntelligent Control Theory");
                                buffer.append("\nEmbedded Systems");
                                break;
                            case 8:
                                buffer.append("Elective III \n");
                                buffer.append("\nHigh Performance Electric Drives");
                                buffer.append("\nPower Plant Component Design");
                                buffer.append("\nHigh Voltage Transmission System");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nInsulation System Design");
                                buffer.append("\nCyber Security for Smart Grid");
                                buffer.append("\nSubstation Automation");
                                break;
                            case 9:
                                buffer.append("Elective III \n");
                                buffer.append("\nSpeech Processing");
                                buffer.append("\nNano Logic Design");
                                buffer.append("\nComputer Architecture");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nVirtual Instrumentation");
                                buffer.append("\nAdvanced Mobile Communication");
                                buffer.append("\nAdvanced Image and Video Processing");
                                break;
                            case 10:
                                buffer.append("Elective III \n");
                                buffer.append("\nSpeech Processing");
                                buffer.append("\nMicrowave Communication Systems");
                                buffer.append("\nCognitive Radio");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nAntenna Design");
                                buffer.append("\nAdvanced DSP");
                                buffer.append("\nAdvanced Image and Video Processing");
                                break;
                            case 11:
                                buffer.append("Elective III \n");
                                buffer.append("\nProgramming Paradigm for Concurrency Control");
                                buffer.append("\nParallel and Distributed Algorithms");
                                buffer.append("\nBig Data Analytics");
                                buffer.append("\nNetworks Qos and Auditing");
                                buffer.append("\nReal Time Systems");
                                buffer.append("\nReconfigurable Computing");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nEthical Hacking");
                                buffer.append("\nInformation Storage Management");
                                buffer.append("\nDistributed and Cloud Database Systems");
                                buffer.append("\nPattern Recognition");
                                buffer.append("\nGraph Mining");
                                buffer.append("\nMulti Core Architecture and Parallel Algorithms");
                                break;
                            case 12:
                                buffer.append("Elective III \n");
                                buffer.append("\nSoftware Project Management");
                                buffer.append("\nProgramming Paradigm for Concurrency Control");
                                buffer.append("\nParallel and Distributed Algorithms");
                                buffer.append("\nBig Data Analytics");
                                buffer.append("\nReal Time Systems");
                                buffer.append("\n\nElective IV \n");
                                buffer.append("\nNetwork Attacks and Defense Mechanism");
                                buffer.append("\nWeb Services and Service Oriented Architecture");
                                buffer.append("\nDistributed and Cloud Database System");
                                buffer.append("\nPattern Recognition");
                                buffer.append("\nGraph Mining");
                                buffer.append("\nMulti Core Architecture and Parallel Algorithms");
                                break;
                            case 13:
                                buffer.append("Elective III\n");
                                buffer.append("\nSoftware Project Management");
                                buffer.append("\nProgramming Paradigm for Concurrency Control");
                                buffer.append("\nSoft Computing");
                                buffer.append("\nReal Time Systems");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nNetwork Attacks and Defense Mechanism");
                                buffer.append("\nDistributed and Cloud Database Systems");
                                buffer.append("\nSoftware Testing");
                                buffer.append("\nPattern Recognition");
                                buffer.append("\nGraph Mining");
                                buffer.append("\nMulti Core Architecture and Parallel Algorithms");
                                break;
                            case 16:
                                buffer.append("Elective III\n");
                                buffer.append("\nApparel Marketing and Merchandizing");
                                buffer.append("\nSustainable Textiles");
                                buffer.append("\n\nElective IV\n");
                                buffer.append("\nAdvances in Manmade Fibre Production");
                                buffer.append("\nSurface Modification of Textiles");
                                break;
                        }
                    default:
                        buffer.append("No Elective subjects for this Semester.");
                        break;
                }
            case 3:
                switch (id2 % 10) {
                    case 2:
                        buffer.append("Elective I \n");
                        buffer.append("\nService Oriented Architecture");
                        buffer.append("\nIT Infrastructure Management");
                        buffer.append("\nGeographical Information System");
                        buffer.append("\nComputer Graphics");
                        buffer.append("\nEnterprise System");
                        break;
                    case 3:
                        buffer.append("Elective II \n");
                        buffer.append("\nImage Processing");
                        buffer.append("\nE-Commerce");
                        buffer.append("\nUnified Communication");
                        buffer.append("\nBig Data Analytics");
                        buffer.append("\nGaming Technology");
                        buffer.append("\nMultimedia Systems");
                        buffer.append("\n\nElective III \n");
                        buffer.append("\nArtificial Intelligence and Robotics");
                        buffer.append("\nInformation Technology in Manufacturing");
                        buffer.append("\nSpeech and Natural Language Processing");
                        buffer.append("\nInternet of Things");
                        buffer.append("\nNeural Networks");
                        break;
                    default:
                        buffer.append("No Elective subjects for this Semester.");
                        break;
                }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle((CharSequence) "Elective Subjects");
        builder.setMessage((CharSequence) buffer);
        builder.show();
    }

    /* JADX WARNING: type inference failed for: r13v0, types: [com.re.developers.vjtispi.calculatornew.ResultScreen] */
    public void createDatabase(int studentcode) {
        switch (studentcode) {
            case 1000:
                boolean result = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 1, "Applied Physics I", 3.0f);
                boolean result2 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 2, "Applied Physics I Lab", 0.75f);
                boolean result3 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 3, "Applied Chemistry I", 3.0f);
                boolean result4 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 4, "Applied Chemistry I Lab", 0.75f);
                boolean result5 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 5, "Mathematics for Engineers I", 4.0f);
                boolean result6 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 6, "Basic Electrical Engineering", 4.0f);
                boolean result7 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 7, "Basic Electrical Engineering Lab", 1.0f);
                boolean result8 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 8, "Engineering Mechanics", 4.0f);
                boolean result9 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 9, "Engineering Mechanics Lab", 1.0f);
                boolean result10 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 10, "Elements of Civil Engineering", 2.0f);
                boolean result11 = this.myDB.InsertData("B.Tech", "CIVIL", "I", 1000, 11, "Workshop Practice", 1.5f);
                return;
            case PointerIconCompat.TYPE_CONTEXT_MENU:
                boolean result12 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 1, "Applied Physics II", 3.0f);
                boolean result13 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 2, "Applied Physics II Lab", 0.75f);
                boolean result14 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 3, "Applied Chemistry II", 3.0f);
                boolean result15 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 4, "Applied Chemistry II Lab", 0.75f);
                boolean result16 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 5, "Mathematics for Engineers II", 4.0f);
                boolean result17 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 6, "Computer Programming and Problem Solving", 4.0f);
                boolean result18 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 7, "Computer Programming and Problem Solving Lab", 1.0f);
                boolean result19 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 8, "Engineering Graphics", 4.0f);
                boolean result20 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 9, "Engineering Graphics Lab", 1.0f);
                boolean result21 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 10, "Elements of Mechanical Engineering", 2.0f);
                boolean result22 = this.myDB.InsertData("B.Tech", "CIVIL", "II", PointerIconCompat.TYPE_CONTEXT_MENU, 11, "Workshop Practice", 1.5f);
                return;
            case PointerIconCompat.TYPE_HAND:
                boolean result23 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 1, "Mathematics for Civil Engineers", 4.0f);
                boolean result24 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 2, "Mechanics of Solids", 3.0f);
                boolean result25 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 3, "Mechanics of Solids Lab", 1.0f);
                boolean result26 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 4, "Construction Engineering and Infrastructure Projects", 3.0f);
                boolean result27 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 5, "Construction Engineering Lab", 1.0f);
                boolean result28 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 6, "Fluid Mechanics", 4.0f);
                boolean result29 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 7, "Fluid Mechanics Lab", 1.0f);
                boolean result30 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 8, "Geomatics", 3.0f);
                boolean result31 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 9, "Geomatics Lab", 1.5f);
                boolean result32 = this.myDB.InsertData("B.Tech", "CIVIL", "III", PointerIconCompat.TYPE_HAND, 10, "Engineering Geology Lab", 1.5f);
                return;
            case PointerIconCompat.TYPE_HELP:
                boolean result33 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 1, "Statistics and Vector Calculus", 4.0f);
                boolean result34 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 2, "Structural Analysis-I", 4.0f);
                boolean result35 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 3, "Soil Mechanics", 3.0f);
                boolean result36 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 4, "Soil Mechanics Lab", 1.5f);
                boolean result37 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 5, "Construction Techniques", 3.0f);
                boolean result38 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 6, "Environmental Studies", 3.0f);
                boolean result39 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 7, "Applied Hydraulics", 3.0f);
                boolean result40 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 8, "Applied Hydraulics Lab", 1.5f);
                boolean result41 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 9, "Geospatial Lab", 1.0f);
                boolean result42 = this.myDB.InsertData("B.Tech", "CIVIL", "IV", PointerIconCompat.TYPE_HELP, 10, "Construction Material Lab", 1.0f);
                return;
            case PointerIconCompat.TYPE_WAIT:
                boolean result43 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 1, "Structural Analysis-II", 4.0f);
                boolean result44 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 2, "Geotechnical Engineering", 3.0f);
                boolean result45 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 3, "Numerical Methods in Civil Engineering", 3.0f);
                boolean result46 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 4, "Engineering Hydrology", 3.0f);
                boolean result47 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 5, "Concrete Technology", 3.0f);
                boolean result48 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 6, "Concrete and Soil Lab", 1.5f);
                boolean result49 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 7, "Building Drawing and Services", 3.0f);
                boolean result50 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 8, "Building Drawing and Services Lab", 1.0f);
                boolean result51 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 9, "Communications and Presentations Skill Lab", 2.0f);
                boolean result52 = this.myDB.InsertData("B.Tech", "CIVIL", "V", PointerIconCompat.TYPE_WAIT, 10, "Computer Applications Lab", 1.5f);
                return;
            case 1005:
                boolean result53 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 1, "Design of RCC & PSC Structure", 3.0f);
                boolean result54 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 2, "Design of RCC Lab", 1.5f);
                boolean result55 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 3, "Environmental Engineering", 3.0f);
                boolean result56 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 4, "Water Resources Engineering", 2.0f);
                boolean result57 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 5, "Qunatity Survey and Estimation", 3.0f);
                boolean result58 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 6, "Qunatity Survey and Estimation Lab", 1.0f);
                boolean result59 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 7, "Pavement Engineering", 3.0f);
                boolean result60 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 8, "Transportation Engineering Lab", 1.0f);
                boolean result61 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 9, "Elective-I", 3.0f);
                boolean result62 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 10, "Elective-I Lab", 1.0f);
                boolean result63 = this.myDB.InsertData("B.Tech", "CIVIL", "VI", 1005, 11, "Site Visit", 1.5f);
                return;
            case PointerIconCompat.TYPE_CELL:
                boolean result64 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 1, "Design of Steel Structures", 3.0f);
                boolean result65 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 2, "Design of Steel Structures Lab", 1.0f);
                boolean result66 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 3, "Transportation Engineering", 3.0f);
                boolean result67 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 4, "Water and Wastewater Engineering", 3.0f);
                boolean result68 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 5, "Water and Wastewater Engineering Lab", 1.0f);
                boolean result69 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 6, "Elective-II", 3.0f);
                boolean result70 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 7, "Open Elective", 4.0f);
                boolean result71 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 8, "Project I", 2.0f);
                boolean result72 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 9, "Industry Internship", 2.0f);
                boolean result73 = this.myDB.InsertData("B.Tech", "CIVIL", "VII", PointerIconCompat.TYPE_CELL, 10, "Development Engineering", 1.0f);
                return;
            case PointerIconCompat.TYPE_CROSSHAIR:
                boolean result74 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 1, "Earthquake Engineering", 3.0f);
                boolean result75 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 2, "Structural Design Lab", 1.0f);
                boolean result76 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 3, "Construction Entrepreneurship", 3.0f);
                boolean result77 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 4, "Construction Management", 3.0f);
                boolean result78 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 5, "Construction Management Lab", 1.0f);
                boolean result79 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 6, "Elective III", 3.0f);
                boolean result80 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 7, "Elective IV", 4.0f);
                boolean result81 = this.myDB.InsertData("B.Tech", "CIVIL", "VIII", PointerIconCompat.TYPE_CROSSHAIR, 8, "Project II", 4.0f);
                return;
            case PointerIconCompat.TYPE_NO_DROP:
                boolean result82 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 1, "Mathematics for Mechanical Engineers I", 4.0f);
                boolean result83 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 2, "Fundamentals of Thermodynamics", 4.0f);
                boolean result84 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 3, "Mechanics of Solids", 3.0f);
                boolean result85 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 4, "Mechanics of Solids Lab", 1.0f);
                boolean result86 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 5, "Electrical Machines and Drives", 3.0f);
                boolean result87 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 6, "Electrical Machines and Drives Lab", 1.0f);
                boolean result88 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 7, "Kinematics of Machinery", 4.0f);
                boolean result89 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 8, "Material Science", 3.0f);
                boolean result90 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 9, "Material Science Lab", 1.0f);
                boolean result91 = this.myDB.InsertData("B.Tech", "MECHANICAL", "III", PointerIconCompat.TYPE_NO_DROP, 10, "Computer Aided Machine Drawing Lab", 2.0f);
                return;
            case PointerIconCompat.TYPE_ALL_SCROLL:
                boolean result92 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 1, "Mathematics for Mechanical Engineers II", 4.0f);
                boolean result93 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 2, "Environmental Studies", 3.0f);
                boolean result94 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 3, "Dynamics of Machinery", 3.0f);
                boolean result95 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 4, "Dynamics of Machinery Lab", 1.0f);
                boolean result96 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 5, "Applied Thermodynamics", 3.0f);
                boolean result97 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 6, "Applied Thermodynamics Lab", 1.0f);
                boolean result98 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 7, "Fluid Mechanics", 3.0f);
                boolean result99 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 8, "Fluid Mechanics Lab", 1.0f);
                boolean result100 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 9, "Manufacturing Processes", 3.0f);
                boolean result101 = this.myDB.InsertData("B.Tech", "MECHANICAL", "IV", PointerIconCompat.TYPE_ALL_SCROLL, 10, "Manufacturing Processes Lab", 1.0f);
                return;
            case PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW:
                boolean result102 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 1, "Numerical Methods for Mechanical Engineers", 4.0f);
                boolean result103 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 2, "Measurement and Control Systems", 3.0f);
                boolean result104 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 3, "Measurement and Control Systems Lab", 1.0f);
                boolean result105 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 4, "Mechanical Vibrations", 3.0f);
                boolean result106 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 5, "Mechanical Vibrations Lab", 1.0f);
                boolean result107 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 6, "Fluid Machinery", 3.0f);
                boolean result108 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 7, "Fluid Machinery Lab", 1.0f);
                boolean result109 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 8, "Manufacturing Technology", 3.0f);
                boolean result110 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 9, "Manufacturing Technology Lab", 1.0f);
                boolean result111 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 10, "Heat Transfer", 3.0f);
                boolean result112 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 11, "Heat Transfer Lab", 1.0f);
                boolean result113 = this.myDB.InsertData("B.Tech", "MECHANICAL", "V", PointerIconCompat.TYPE_HORIZONTAL_DOUBLE_ARROW, 12, "Communication and Presentation Skills", 2.0f);
                return;
            case PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW:
                boolean result114 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 1, "Machine Design", 4.0f);
                boolean result115 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 2, "Metrology and Quality Control", 4.0f);
                boolean result116 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 3, "CAD/CAM", 3.0f);
                boolean result117 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 4, "CAD/CAM Lab", 1.0f);
                boolean result118 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 5, "Internal Combustion Engines", 3.0f);
                boolean result119 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 6, "Internal Combustion Engines Lab", 1.0f);
                boolean result120 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 7, "Refrigeration and Air Conditioning", 3.0f);
                boolean result121 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 8, "Refrigeration and Air Conditioning Lab", 1.0f);
                boolean result122 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 9, "Elective I", 3.0f);
                boolean result123 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 10, "Elective I Lab", 1.0f);
                boolean result124 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VI", PointerIconCompat.TYPE_VERTICAL_DOUBLE_ARROW, 11, "Advanced Business Communication", 1.0f);
                return;
            case PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW:
                boolean result125 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 1, "Design of Power Transmission Elements", 4.0f);
                boolean result126 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 2, "Mechatronics", 3.0f);
                boolean result127 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 3, "Mechatronics Lab", 1.0f);
                boolean result128 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 4, "Manufacturing Planning and Control", 3.0f);
                boolean result129 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 5, "Elective II", 3.0f);
                boolean result130 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 6, "Elective II Lab", 1.0f);
                boolean result131 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 7, "Open Elective", 4.0f);
                boolean result132 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VII", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 8, "Project I", 2.0f);
                return;
            case PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW:
                boolean result133 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 1, "Industrial Engineering", 3.0f);
                boolean result134 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 2, "Design of Mechanical System", 3.0f);
                boolean result135 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 3, "Energy Engineering", 3.0f);
                boolean result136 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 4, "Energy Engineering Lab", 1.0f);
                boolean result137 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 5, "Elective III", 3.0f);
                boolean result138 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 6, "Elective III Lab", 1.0f);
                boolean result139 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 7, "Elective IV", 3.0f);
                boolean result140 = this.myDB.InsertData("B.Tech", "MECHANICAL", "VIII", PointerIconCompat.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW, 8, "Project II", 4.0f);
                return;
            case 1022:
                boolean result141 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 1, "Mathematics for Electrical Engineers I", 4.0f);
                boolean result142 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 2, "Network Analysis and Circuit Theory", 4.0f);
                boolean result143 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 3, "Environmental Studies", 3.0f);
                boolean result144 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 4, "Electronics Devices and Circuits I", 3.0f);
                boolean result145 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 5, "Electronics Devices and Circuits I Lab", 1.0f);
                boolean result146 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 6, "Electrical Machines I", 3.0f);
                boolean result147 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 7, "Electrical Machines I Lab", 1.0f);
                boolean result148 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 8, "Numerical Methods", 3.0f);
                boolean result149 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "III", 1022, 9, "Numerical Methods Lab", 1.0f);
                return;
            case 1023:
                boolean result150 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 1, "Mathematics for Electrical Engineers II", 4.0f);
                boolean result151 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 2, "Introduction to Power Systems", 4.0f);
                boolean result152 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 3, "Signals and Systems", 4.0f);
                boolean result153 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 4, "Network Synthesis", 3.0f);
                boolean result154 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 5, "Network Synthesis Lab", 1.5f);
                boolean result155 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 6, "Electrical Machines II", 3.0f);
                boolean result156 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 7, "Electrical Machines II Lab", 1.5f);
                boolean result157 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 8, "Electronics Devices and Circuits II", 3.0f);
                boolean result158 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 9, "Electronics Devices and Circuits II Lab", 1.0f);
                boolean result159 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "IV", 1023, 10, "Presentation and Communication Skills", 2.0f);
                return;
            case 1024:
                boolean result160 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 1, "Mathematics for Electrical Engineers III", 4.0f);
                boolean result161 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 2, "Electromagnetic Fields and Waves", 4.0f);
                boolean result162 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 3, "Power System Protection", 4.0f);
                boolean result163 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 4, "Power Electronics", 3.0f);
                boolean result164 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 5, "Power Electronics Lab", 1.0f);
                boolean result165 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 6, "Control System", 3.0f);
                boolean result166 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 7, "Control System Lab", 1.0f);
                boolean result167 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 8, "Analog and Digital Electronics", 3.0f);
                boolean result168 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "V", 1024, 9, "Analog and Digital Electronics Lab", 1.0f);
                return;
            case InputDeviceCompat.SOURCE_GAMEPAD:
                boolean result169 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 1, "Advanced Control System", 4.0f);
                boolean result170 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 2, "Power System Analysis", 4.0f);
                boolean result171 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 3, "Electrical and Electronics Measurement and Instrumentation", 4.0f);
                boolean result172 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 4, "Microprocessor and Microcontroller", 3.0f);
                boolean result173 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 5, "Microprocessor and Microcontroller Lab", 1.0f);
                boolean result174 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 6, "Communication System", 3.0f);
                boolean result175 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 7, "Communication System Lab", 1.0f);
                boolean result176 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 8, "Elective I", 3.0f);
                boolean result177 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VI", InputDeviceCompat.SOURCE_GAMEPAD, 9, "Elective I Lab", 1.0f);
                return;
            case 1026:
                boolean result178 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 1, "Power Quality", 4.0f);
                boolean result179 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 2, "Digital Signal Processing", 4.0f);
                boolean result180 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 3, "Drives and Control", 4.0f);
                boolean result181 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 4, "Drives and Control Lab", 1.0f);
                boolean result182 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 5, "Elective II", 3.0f);
                boolean result183 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 6, "Elective II Lab", 1.0f);
                boolean result184 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 7, "Open Elective", 4.0f);
                boolean result185 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VII", 1026, 8, "Project I", 2.0f);
                return;
            case 1027:
                boolean result186 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 1, "Electrical Machine Design", 4.0f);
                boolean result187 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 2, "Optimization Techniques", 4.0f);
                boolean result188 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 3, "High Voltage Engineering", 3.0f);
                boolean result189 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 4, "High Voltage Engineering Lab", 1.0f);
                boolean result190 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 5, "Elective III", 4.0f);
                boolean result191 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 6, "Elective III Lab", 1.0f);
                boolean result192 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 7, "Elective IV", 4.0f);
                boolean result193 = this.myDB.InsertData("B.Tech", "ELECTRICAL", "VIII", 1027, 8, "Project II", 4.0f);
                return;
            case 1032:
                boolean result194 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 1, "Mathematics for Production Engineers I", 4.0f);
                boolean result195 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 2, "Material Science", 4.0f);
                boolean result196 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 3, "Mechanics of Solids", 3.0f);
                boolean result197 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 4, "Mechanics of Solids Lab", 1.0f);
                boolean result198 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 5, "Basic Thermodynamics and Heat Transfer", 4.0f);
                boolean result199 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 6, "Fluid Mechanics and Machinery", 3.0f);
                boolean result200 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 7, "Fluid Mechanics and Machinery Lab", 1.0f);
                boolean result201 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 8, "Production and Machine Drawing", 3.0f);
                boolean result202 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 9, "Manufacturing Engineering", 3.0f);
                boolean result203 = this.myDB.InsertData("B.Tech", "PRODUCTION", "III", 1032, 10, "Workshop Practices", 1.0f);
                return;
            case 1033:
                boolean result204 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 1, "Mathematics for Production Engineers II", 4.0f);
                boolean result205 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 2, "Metal Casting and Joining", 3.0f);
                boolean result206 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 3, "Metal Casting and Joining Lab", 1.0f);
                boolean result207 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 4, "Theory of Machines", 4.0f);
                boolean result208 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 5, "Environmental Studies", 3.0f);
                boolean result209 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 6, "Applied Thermodynamics", 3.0f);
                boolean result210 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 7, "Applied Thermodynamics Lab", 1.0f);
                boolean result211 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 8, "Electrical and Electronics Engineering", 3.0f);
                boolean result212 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 9, "Electrical and Electronics Engineering Lab", 1.0f);
                boolean result213 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 10, "Metrology and Quality Control", 3.0f);
                boolean result214 = this.myDB.InsertData("B.Tech", "PRODUCTION", "IV", 1033, 11, "Metrology and Quality Control Lab", 1.0f);
                return;
            case 1034:
                boolean result215 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 1, "Probability and Statistics", 4.0f);
                boolean result216 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 2, "Elements of Project Management", 4.0f);
                boolean result217 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 3, "Machine Design", 4.0f);
                boolean result218 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 4, "Communication and Presentation Skills", 2.0f);
                boolean result219 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 5, "Metal Forming Technology", 3.0f);
                boolean result220 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 6, "Metal Forming Technology Lab", 1.0f);
                boolean result221 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 7, "Machining Science and Technology", 3.0f);
                boolean result222 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 8, "Machining Science and Technology Lab", 1.0f);
                boolean result223 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 9, "Industrial Engineering", 3.0f);
                boolean result224 = this.myDB.InsertData("B.Tech", "PRODUCTION", "V", 1034, 10, "Industrial Engineering Lab", 1.0f);
                return;
            case 1035:
                boolean result225 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 1, "Machine Tool Design", 3.0f);
                boolean result226 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 2, "Machine Tool Design Lab", 1.0f);
                boolean result227 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 3, "Automobile Engineering", 4.0f);
                boolean result228 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 4, "CAD/CAM/CIM", 3.0f);
                boolean result229 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 5, "CAD/CAM/CIM Lab", 1.0f);
                boolean result230 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 6, "Tools Engineering", 3.0f);
                boolean result231 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 7, "Tools Engineering Lab", 1.0f);
                boolean result232 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 8, "Mechatronics and Automation", 3.0f);
                boolean result233 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 9, "Mechatronics and Automation Lab", 1.0f);
                boolean result234 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 10, "Elective I", 3.0f);
                boolean result235 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VI", 1035, 11, "Elective II", 3.0f);
                return;
            case 1036:
                boolean result236 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VII", 1036, 1, "Industrial Training and Project", 25.0f);
                return;
            case 1037:
                boolean result237 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 1, "Economics, Finance, Accounting and Costing", 4.0f);
                boolean result238 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 2, "Supply Chain Management", 4.0f);
                boolean result239 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 3, "Operation Research", 4.0f);
                boolean result240 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 4, "Composite Material and Processing Technology", 3.0f);
                boolean result241 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 5, "Statistical Analysis Lab", 1.0f);
                boolean result242 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 6, "Organizational Behavior", 3.0f);
                boolean result243 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 7, "Optimization Lab", 1.0f);
                boolean result244 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 8, "Elective I", 3.0f);
                boolean result245 = this.myDB.InsertData("B.Tech", "PRODUCTION", "VIII", 1037, 9, "Elective I Lab", 1.0f);
                return;
            case 1042:
                boolean result246 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 1, "Mathematics for Textile Technology", 4.0f);
                boolean result247 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 2, "Mechanics of Textile Machinery", 4.0f);
                boolean result248 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 3, "Textile Fibres", 4.0f);
                boolean result249 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 4, "Spinning Preparatory", 3.0f);
                boolean result250 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 5, "Spinning Preparatory Lab", 1.0f);
                boolean result251 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 6, "Introduction to Fabric Manufacturing", 3.0f);
                boolean result252 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 7, "Introduction to Fabric Manufacturing Lab", 1.0f);
                boolean result253 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 8, "Textile Pretreatments and Dyeing", 3.0f);
                boolean result254 = this.myDB.InsertData("B.Tech", "TEXTILE", "III", 1042, 9, "Textile Pretreatments and Dyeing Lab", 1.0f);
                return;
            case 1043:
                boolean result255 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 1, "Statistics for Textile Technology", 4.0f);
                boolean result256 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 2, "Textile Printing and Finishing", 4.0f);
                boolean result257 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 3, "Environmental Studies", 3.0f);
                boolean result258 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 4, "Spinning Technology", 4.0f);
                boolean result259 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 5, "Spinning Technology Lab", 1.0f);
                boolean result260 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 6, "Weaving Technology", 3.0f);
                boolean result261 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 7, "Weaving Technology Lab", 1.0f);
                boolean result262 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 8, "Testing of Fibre and Yarn", 3.0f);
                boolean result263 = this.myDB.InsertData("B.Tech", "TEXTILE", "IV", 1043, 9, "Testing of Fibre and Yarn Lab", 1.0f);
                return;
            case 1044:
                boolean result264 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 1, "Textile Calculations", 4.0f);
                boolean result265 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 2, "Electrical and Electronic Devices in Textile", 4.0f);
                boolean result266 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 3, "Cloth Structure", 4.0f);
                boolean result267 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 4, "Advanced Yarn Manufacturing", 3.0f);
                boolean result268 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 5, "Advanced Yarn Manufacturing Lab", 1.0f);
                boolean result269 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 6, "Advanced Fabric Manufacturing", 3.0f);
                boolean result270 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 7, "Advanced Fabric Manufacturing Lab", 1.0f);
                boolean result271 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 8, "Textile Testing", 3.0f);
                boolean result272 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 9, "Textile Testing Lab", 1.0f);
                boolean result273 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 10, "Advanced Cloth Structure Analysis", 2.0f);
                boolean result274 = this.myDB.InsertData("B.Tech", "TEXTILE", "V", 1044, 11, "Presentation and Communication Skills", 2.0f);
                return;
            case 1045:
                boolean result275 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 1, "Man-Made Fibre Production", 4.0f);
                boolean result276 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 2, "Technical Textiles", 4.0f);
                boolean result277 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 3, "Design of Textiles Structures", 4.0f);
                boolean result278 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 4, "Knitting Technology", 3.0f);
                boolean result279 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 5, "Knitting Technology Lab", 1.0f);
                boolean result280 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 6, "Apparel Manufacturing and Merchandising", 3.0f);
                boolean result281 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 7, "Apparel Manufacturing and Merchandising Lab", 1.0f);
                boolean result282 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 8, "Elective I", 3.0f);
                boolean result283 = this.myDB.InsertData("B.Tech", "TEXTILE", "VI", 1045, 9, "Elective I Lab", 1.0f);
                return;
            case 1046:
                boolean result284 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 1, "Non-Woven and Industrial Textiles", 4.0f);
                boolean result285 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 2, "Long Staple Spinning and Weaving", 4.0f);
                boolean result286 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 3, "Textile Composites", 3.0f);
                boolean result287 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 4, "Textile Composites Lab", 1.0f);
                boolean result288 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 5, "Elective II", 4.0f);
                boolean result289 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 6, "Elective II Lab", 1.0f);
                boolean result290 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 7, "Open Elective", 4.0f);
                boolean result291 = this.myDB.InsertData("B.Tech", "TEXTILE", "VII", 1046, 8, "Project I", 2.0f);
                return;
            case 1047:
                boolean result292 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 1, "Textile Management", 4.0f);
                boolean result293 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 2, "Sustainable Textile Material", 4.0f);
                boolean result294 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 3, "Process Control in Textile Manufacturing", 4.0f);
                boolean result295 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 4, "Process Control in Textile Manufacturing Lab", 1.0f);
                boolean result296 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 5, "Elective III", 3.0f);
                boolean result297 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 6, "Elective III Lab", 1.0f);
                boolean result298 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 7, "Elective IV", 4.0f);
                boolean result299 = this.myDB.InsertData("B.Tech", "TEXTILE", "VIII", 1047, 8, "Project II", 4.0f);
                return;
            case 1052:
                boolean result300 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 1, "Mathematics for Electrical Engineers I", 4.0f);
                boolean result301 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 2, "Environmental Studies", 3.0f);
                boolean result302 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 3, "Electrical Networks", 4.0f);
                boolean result303 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 4, "Electronics Circuits Analysis and Design I", 3.0f);
                boolean result304 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 5, "Electronics Circuits Analysis and Design I Lab", 1.0f);
                boolean result305 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 6, "Digital Combinational Circuits", 3.0f);
                boolean result306 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 7, "Digital Combinational Circuits Lab", 1.0f);
                boolean result307 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 8, "Numerical Techniques", 3.0f);
                boolean result308 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "III", 1052, 9, "Numerical Techniques Lab", 1.0f);
                return;
            case 1053:
                boolean result309 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 1, "Mathematics for Electrical Engineers II", 4.0f);
                boolean result310 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 2, "Signals and Systems", 4.0f);
                boolean result311 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 3, "Digital Sequential Circuits", 3.0f);
                boolean result312 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 4, "Digital Sequential Circuits Lab", 1.0f);
                boolean result313 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 5, "Electronics Circuits Analysis and Design II", 3.0f);
                boolean result314 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 6, "Electronics Circuits Analysis and Design II Lab", 1.0f);
                boolean result315 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 7, "Measurements and Instrumentation", 3.0f);
                boolean result316 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 8, "Measurements and Instrumentation Lab", 1.0f);
                boolean result317 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 9, "Microprocessor and Microcontroller", 3.0f);
                boolean result318 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 10, "Microprocessor and Microcontroller Lab", 1.0f);
                boolean result319 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "IV", 1053, 11, "Presentation and Communication Skills", 2.0f);
                return;
            case 1054:
                boolean result320 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 1, "Principles of Communication Systems", 3.0f);
                boolean result321 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 2, "Principles of Communication Systems Lab", 1.0f);
                boolean result322 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 3, "Probability and Statistics", 4.0f);
                boolean result323 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 4, "Electromagnetic Field and Waves", 4.0f);
                boolean result324 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 5, "Analog Integrated Circuits", 3.0f);
                boolean result325 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 6, "Analog Integrated Circuits Lab", 1.0f);
                boolean result326 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 7, "Microprocessor Systems", 3.0f);
                boolean result327 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 8, "Microprocessor Systems Lab", 1.0f);
                boolean result328 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 9, "Power Electronics", 3.0f);
                boolean result329 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 10, "Power Electronics Lab", 1.0f);
                boolean result330 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "V", 1054, 11, "Electronic Engineering Practice", 1.0f);
                return;
            case 1055:
                boolean result331 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 1, "Control Systems", 3.0f);
                boolean result332 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 2, "Computer Organization", 3.0f);
                boolean result333 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 3, "Computer Organization Lab", 1.0f);
                boolean result334 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 4, "Digital Communication", 3.0f);
                boolean result335 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 5, "Digital Communication Lab", 1.0f);
                boolean result336 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 5, "Filter Theory", 3.0f);
                boolean result337 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 6, "Filter Theory Lab", 1.0f);
                boolean result338 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 7, "Electronics in Service to Society", 2.0f);
                boolean result339 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 8, "Advanced Business Communication", 1.0f);
                boolean result340 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 9, "Transducers and Sensors", 3.0f);
                boolean result341 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 10, "Elective I", 3.0f);
                boolean result342 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VI", 1055, 11, "Elective I Lab", 1.0f);
                return;
            case 1056:
                boolean result343 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 1, "Microwave and Optical Communication", 4.0f);
                boolean result344 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 2, "Data Communication and Networking", 3.0f);
                boolean result345 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 3, "Data Communication and Networking Lab", 1.0f);
                boolean result346 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 4, "Embedded Systems", 3.0f);
                boolean result347 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 5, "Embedded Systems Lab", 1.0f);
                boolean result348 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 6, "Elective II", 4.0f);
                boolean result349 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 7, "Elective II Lab", 1.0f);
                boolean result350 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 8, "Open Elective", 4.0f);
                boolean result351 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VII", 1056, 9, "Project I", 2.0f);
                return;
            case 1057:
                boolean result352 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 1, "Wireless Communication", 3.0f);
                boolean result353 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 2, "Wireless Communication Lab", 1.0f);
                boolean result354 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 3, "Basics of VLSI", 4.0f);
                boolean result355 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 4, "Basics of VLSI Lab", 1.0f);
                boolean result356 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 5, "Advanced Digital Signal Processing", 3.0f);
                boolean result357 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 6, "Advanced Digital Signal Processing Lab", 1.0f);
                boolean result358 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 7, "Elective III", 3.0f);
                boolean result359 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 8, "Elective III Lab", 1.0f);
                boolean result360 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 9, "Elective IV", 4.0f);
                boolean result361 = this.myDB.InsertData("B.Tech", "ELECTRONICS", "VIII", 1057, 10, "Project II", 4.0f);
                return;
            case 1062:
                boolean result362 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 1, "Mathematics for Computer Engineers I", 4.0f);
                boolean result363 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 2, "Discrete Mathematics", 4.0f);
                boolean result364 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 3, "Environmental Studies", 3.0f);
                boolean result365 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 4, "Data Structures", 3.0f);
                boolean result366 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 5, "Data Structures Lab", 1.0f);
                boolean result367 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 6, "Program Development using Object Oriented Paradigm", 3.0f);
                boolean result368 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 7, "Program Development using Object Oriented Paradigm Lab", 1.0f);
                boolean result369 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 8, "Digital Logic Design", 3.0f);
                boolean result370 = this.myDB.InsertData("B.Tech", "COMPUTER", "III", 1062, 9, "Digital Logic Design Lab", 1.0f);
                return;
            case 1063:
                boolean result371 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 1, "Mathematics for Computer Engineers II", 4.0f);
                boolean result372 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 2, "Algorithm Design", 4.0f);
                boolean result373 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 3, "Theory of Computer Science", 4.0f);
                boolean result374 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 4, "Data Communication and Networking", 3.0f);
                boolean result375 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 5, "Data Communication and Networking Lab", 1.0f);
                boolean result376 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 6, "Computer Organization and Architecture", 3.0f);
                boolean result377 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 7, "Computer Organization and Architecture Lab", 1.0f);
                boolean result378 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 8, "Fundamentals of Database Systems", 3.0f);
                boolean result379 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 9, "Fundamentals of Database Systems Lab", 1.0f);
                boolean result380 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 10, "Presentation and Communication Skills", 2.0f);
                boolean result381 = this.myDB.InsertData("B.Tech", "COMPUTER", "IV", 1063, 11, "Open Source Technologies", 2.0f);
                return;
            case 1064:
                boolean result382 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 1, "Statistics and Optimization", 4.0f);
                boolean result383 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 2, "Artificial Intelligence", 4.0f);
                boolean result384 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 3, "Software Engineering", 3.0f);
                boolean result385 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 4, "Software Engineering Lab", 1.0f);
                boolean result386 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 5, "Computer Network Systems", 3.0f);
                boolean result387 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 6, "Computer Network Systems Lab", 1.0f);
                boolean result388 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 7, "Introduction to Operating Systems", 4.0f);
                boolean result389 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 8, "Internet of Things", 3.0f);
                boolean result390 = this.myDB.InsertData("B.Tech", "COMPUTER", "V", 1064, 9, "Internet of Things Lab", 1.0f);
                return;
            case 1065:
                boolean result391 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 1, "Object Oriented Modeling and Design", 4.0f);
                boolean result392 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 2, "Machine Learning", 4.0f);
                boolean result393 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 3, "Parallel Computer Architecture", 3.0f);
                boolean result394 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 4, "Parallel Computer Architecture Lab", 1.0f);
                boolean result395 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 5, "Cryptography and Network Security", 3.0f);
                boolean result396 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 6, "Cryptography and Network Security Lab", 1.0f);
                boolean result397 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 7, "Elective I", 4.0f);
                boolean result398 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 8, "Compiler Construction", 3.0f);
                boolean result399 = this.myDB.InsertData("B.Tech", "COMPUTER", "VI", 1065, 9, "Compiler Construction Lab", 1.0f);
                return;
            case 1066:
                boolean result400 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 1, "Software Architecture", 4.0f);
                boolean result401 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 2, "Information Security", 3.0f);
                boolean result402 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 3, "Information Security Lab", 1.0f);
                boolean result403 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 4, "Data Mining and Data Warehousing", 3.0f);
                boolean result404 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 5, "Data Mining and Data Warehousing Lab", 1.0f);
                boolean result405 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 6, "Elective II", 4.0f);
                boolean result406 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 7, "Elective II Lab", 1.0f);
                boolean result407 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 8, "Open Elective", 4.0f);
                boolean result408 = this.myDB.InsertData("B.Tech", "COMPUTER", "VII", 1066, 9, "Project I", 2.0f);
                return;
            case 1067:
                boolean result409 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 1, "Big Data Analytics", 4.0f);
                boolean result410 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 2, "Cloud Computing", 3.0f);
                boolean result411 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 3, "Cloud Computing Lab", 1.0f);
                boolean result412 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 4, "Human Computer Interface", 3.0f);
                boolean result413 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 5, "Human Computer Interface Lab", 1.0f);
                boolean result414 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 6, "Elective III", 3.0f);
                boolean result415 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 7, "Elective III Lab", 1.0f);
                boolean result416 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 8, "Elective IV", 4.0f);
                boolean result417 = this.myDB.InsertData("B.Tech", "COMPUTER", "VIII", 1067, 9, "Project II", 4.0f);
                return;
            case 1070:
                boolean result418 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 1, "Applied Physics I", 3.0f);
                boolean result419 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 2, "Applied Physics I Lab", 0.75f);
                boolean result420 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 3, "Applied Chemistry I", 3.0f);
                boolean result421 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 4, "Applied Chemistry I Lab", 0.75f);
                boolean result422 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 5, "Mathematics for Engineers I", 4.0f);
                boolean result423 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 6, "Computer Programming and Problem Solving", 4.0f);
                boolean result424 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 7, "Computer Programming and Problem Solving Lab", 1.0f);
                boolean result425 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 8, "Engineering Graphics", 4.0f);
                boolean result426 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 9, "Engineering Graphics Lab", 1.0f);
                boolean result427 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 10, "Elements of Mechanical Engineering", 2.0f);
                boolean result428 = this.myDB.InsertData("B.Tech", "IT", "I", 1070, 11, "Workshop Practice", 1.5f);
                return;
            case 1071:
                boolean result429 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 1, "Applied Physics II", 3.0f);
                boolean result430 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 2, "Applied Physics II Lab", 0.75f);
                boolean result431 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 3, "Applied Chemistry II", 3.0f);
                boolean result432 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 4, "Applied Chemistry II Lab", 0.75f);
                boolean result433 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 5, "Mathematics for Engineers II", 4.0f);
                boolean result434 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 6, "Basic Electrical Engineering", 4.0f);
                boolean result435 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 7, "Basic Electrical Engineering Lab", 1.0f);
                boolean result436 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 8, "Engineering Mechanics", 4.0f);
                boolean result437 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 9, "Engineering Mechanics Lab", 1.0f);
                boolean result438 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 10, "Elements of Civil Engineering", 2.0f);
                boolean result439 = this.myDB.InsertData("B.Tech", "IT", "II", 1071, 11, "Workshop Practice", 1.5f);
                return;
            case 1072:
                boolean result440 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 1, "Mathematics for Computer Engineers I", 4.0f);
                boolean result441 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 2, "Discrete Structures", 4.0f);
                boolean result442 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 3, "Environmental Studies", 3.0f);
                boolean result443 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 4, "Fundamentals of Data Structures", 3.0f);
                boolean result444 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 5, "Fundamentals of Data Structures Lab", 1.0f);
                boolean result445 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 6, "Program Development", 3.0f);
                boolean result446 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 7, "Program Development Lab", 1.0f);
                boolean result447 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 8, "Digital Systems", 3.0f);
                boolean result448 = this.myDB.InsertData("B.Tech", "IT", "III", 1072, 9, "Digital Systems Lab", 1.0f);
                return;
            case 1073:
                boolean result449 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 1, "Mathematics for Computer Engineers II", 4.0f);
                boolean result450 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 2, "Design and Analysis of Algorithms", 4.0f);
                boolean result451 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 3, "Theory of Computations", 4.0f);
                boolean result452 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 4, "Data Communication", 3.0f);
                boolean result453 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 5, "Data Communication Lab", 1.0f);
                boolean result454 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 6, "Computer Organization and Architecture", 3.0f);
                boolean result455 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 7, "Computer Organization and Architecture Lab", 1.0f);
                boolean result456 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 8, "Database Management System", 3.0f);
                boolean result457 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 9, "Database Management System Lab", 1.0f);
                boolean result458 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 10, "Presentation and Communication Skills", 2.0f);
                boolean result459 = this.myDB.InsertData("B.Tech", "IT", "IV", 1073, 11, "Open Source Software Lab", 2.0f);
                return;
            case 1074:
                boolean result460 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 1, "Statistics and Optimization", 4.0f);
                boolean result461 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 2, "Intelligent System", 4.0f);
                boolean result462 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 3, "Computer Networks", 3.0f);
                boolean result463 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 4, "Computer Networks Lab", 1.0f);
                boolean result464 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 5, "Software Analysis and Design", 3.0f);
                boolean result465 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 6, "Software Analysis and Design Lab", 1.0f);
                boolean result466 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 7, "Graphics and Processing", 4.0f);
                boolean result467 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 8, "Operating System", 3.0f);
                boolean result468 = this.myDB.InsertData("B.Tech", "IT", "V", 1074, 9, "Operating System Lab", 1.0f);
                return;
            case 1075:
                boolean result469 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 1, "Software Architecture and Design Patterns", 4.0f);
                boolean result470 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 2, "Machine Learning", 4.0f);
                boolean result471 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 3, "Advanced Architecture", 3.0f);
                boolean result472 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 4, "Advanced Architecture Lab", 1.0f);
                boolean result473 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 5, "Wireless Network", 3.0f);
                boolean result474 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 6, "Wireless Network Lab", 1.0f);
                boolean result475 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 7, "Elective I", 4.0f);
                boolean result476 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 8, "Information Security", 3.0f);
                boolean result477 = this.myDB.InsertData("B.Tech", "IT", "VI", 1075, 9, "Information Security Lab", 1.0f);
                return;
            case 1076:
                boolean result478 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 1, "Information Storage and Management", 4.0f);
                boolean result479 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 2, "Data Mining Techniques", 3.0f);
                boolean result480 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 3, "Data Mining Techniques Lab", 1.0f);
                boolean result481 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 4, "Service Oriented Architecture", 3.0f);
                boolean result482 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 5, "Service Oriented Architecture Lab", 1.0f);
                boolean result483 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 6, "Elective II", 4.0f);
                boolean result484 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 7, "Elective II Lab", 1.0f);
                boolean result485 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 8, "Open Elective", 4.0f);
                boolean result486 = this.myDB.InsertData("B.Tech", "IT", "VII", 1076, 9, "Project I", 2.0f);
                return;
            case 1077:
                boolean result487 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 1, "Human Computer Interaction", 4.0f);
                boolean result488 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 2, "Cloud Computing", 3.0f);
                boolean result489 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 3, "Cloud Computing Lab", 1.0f);
                boolean result490 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 4, "Big Data Analytics", 3.0f);
                boolean result491 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 5, "Big Data Analytics Lab", 1.0f);
                boolean result492 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 6, "Elective III", 3.0f);
                boolean result493 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 7, "Elective III Lab", 1.0f);
                boolean result494 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 8, "Elective IV", 4.0f);
                boolean result495 = this.myDB.InsertData("B.Tech", "IT", "VIII", 1077, 9, "Project II", 4.0f);
                return;
            case 1082:
                boolean result496 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 1, "Mathematics for Engineering III", 4.0f);
                boolean result497 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 2, "Environmental Studies", 3.0f);
                boolean result498 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 3, "Network Analysis and Synthesis", 4.0f);
                boolean result499 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 4, "Electronics Circuit Analysis and Design I", 3.0f);
                boolean result500 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 5, "Electronics Circuit Analysis and Design I Lab", 1.0f);
                boolean result501 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 6, "Digital Logic Design", 3.0f);
                boolean result502 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 7, "Digital Logic Design Lab", 1.0f);
                boolean result503 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 8, "JAVA Programming", 3.0f);
                boolean result504 = this.myDB.InsertData("B.Tech", "EXTC", "III", 1082, 9, "JAVA Programming Lab", 1.0f);
                return;
            case 1083:
                boolean result505 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 1, "Mathematics for Engineering IV", 4.0f);
                boolean result506 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 2, "Signals and Systems", 4.0f);
                boolean result507 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 3, "Numerical Techniques", 3.0f);
                boolean result508 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 4, "Numerical Techniques Lab", 1.0f);
                boolean result509 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 5, "Electronic Circuit Analysis and Design II", 3.0f);
                boolean result510 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 6, "Electronic Circuit Analysis and Design II Lab", 1.0f);
                boolean result511 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 7, "Analog Communication", 3.0f);
                boolean result512 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 8, "Analog Communication Lab", 1.0f);
                boolean result513 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 9, "Integrated Circuit and Applications", 3.0f);
                boolean result514 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 10, "Integrated Circuit and Applications Lab", 1.0f);
                boolean result515 = this.myDB.InsertData("B.Tech", "EXTC", "IV", 1083, 11, "Presentation and Communication Skills", 2.0f);
                return;
            case 1084:
                boolean result516 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 1, "Mathematics for Engineering V", 4.0f);
                boolean result517 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 2, "Electromagnetic Fields", 4.0f);
                boolean result518 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 3, "Basics of Control System", 4.0f);
                boolean result519 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 4, "Microprocessor and Microcontroller Systems", 3.0f);
                boolean result520 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 5, "Microprocessor and Microcontroller Systems Lab", 1.0f);
                boolean result521 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 6, "Microwave Engineering", 3.0f);
                boolean result522 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 7, "Microwave Engineering Lab", 1.0f);
                boolean result523 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 8, "VLSI System", 3.0f);
                boolean result524 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 9, "VLSI System Lab", 1.0f);
                boolean result525 = this.myDB.InsertData("B.Tech", "EXTC", "V", 1084, 10, "Communication Circuit Design Lab", 2.0f);
                return;
            case 1085:
                boolean result526 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 1, "Statistical Theory of Communication", 4.0f);
                boolean result527 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 2, "Antenna Design", 4.0f);
                boolean result528 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 3, "Satellite Communication", 4.0f);
                boolean result529 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 4, "RF Circuit Design", 3.0f);
                boolean result530 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 5, "RF Circuit Design Lab", 1.0f);
                boolean result531 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 6, "Digital Communication Systems", 3.0f);
                boolean result532 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 7, "Digital Communication Systems Lab", 1.0f);
                boolean result533 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 8, "Elective I", 3.0f);
                boolean result534 = this.myDB.InsertData("B.Tech", "EXTC", "VI", 1085, 9, "Elective I Lab", 1.0f);
                return;
            case 1086:
                boolean result535 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 1, "Fiber Optic Communication", 4.0f);
                boolean result536 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 2, "Digital Signal Processing", 4.0f);
                boolean result537 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 3, "Mobile Communication Systems", 3.0f);
                boolean result538 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 4, "Mobile Communication Systems Lab", 1.0f);
                boolean result539 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 5, "Elective II", 4.0f);
                boolean result540 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 6, "Elective II Lab", 1.0f);
                boolean result541 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 7, "Open Elective", 4.0f);
                boolean result542 = this.myDB.InsertData("B.Tech", "EXTC", "VII", 1086, 8, "Project I", 2.0f);
                return;
            case 1087:
                boolean result543 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 1, "Embedded Communication System", 4.0f);
                boolean result544 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 2, "Coding Theory", 4.0f);
                boolean result545 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 3, "Data Communication and Networks", 3.0f);
                boolean result546 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 4, "Data Communication and Networks Lab", 1.0f);
                boolean result547 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 5, "Elective III", 3.0f);
                boolean result548 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 6, "Elective III Lab", 1.0f);
                boolean result549 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 7, "Elective IV", 4.0f);
                boolean result550 = this.myDB.InsertData("B.Tech", "EXTC", "VIII", 1087, 8, "Project II", 4.0f);
                return;
            case 2000:
                boolean result551 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 1, "Computational Methods", 4.0f);
                boolean result552 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 2, "Chassis and Body Engineering", 4.0f);
                boolean result553 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 3, "Advanced Internal Combustion Engines", 3.0f);
                boolean result554 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 4, "Advanced Internal Combustion Engines Lab", 1.0f);
                boolean result555 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 5, "Machine Dynamics and Vibration", 3.0f);
                boolean result556 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 6, "Machine Dynamics and Vibration Lab", 1.0f);
                boolean result557 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 7, "Elective I", 4.0f);
                boolean result558 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 8, "Elective II", 3.0f);
                boolean result559 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "I", 2000, 9, "Elective II Lab", 1.0f);
                return;
            case 2001:
                boolean result560 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 1, "Research Methodologies", 4.0f);
                boolean result561 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 2, "Design of Suspension Systems", 4.0f);
                boolean result562 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 3, "Vehicle Dynamics", 3.0f);
                boolean result563 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 4, "Vehicle Dynamics Lab", 1.0f);
                boolean result564 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 5, "Design of Automobile Transmission Systems", 3.0f);
                boolean result565 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 6, "Design of Automobile Transmission Systems Lab", 1.0f);
                boolean result566 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 7, "Elective III", 3.0f);
                boolean result567 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 8, "Elective IV", 3.0f);
                boolean result568 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 9, "Elective IV Lab", 1.0f);
                boolean result569 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "II", 2001, 10, "Technical Seminar", 2.0f);
                return;
            case 2002:
                boolean result570 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "III", 2002, 1, "Stage I Presentation", 4.0f);
                boolean result571 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "III", 2002, 2, "Stage II Presentation", 4.0f);
                return;
            case 2003:
                boolean result572 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "IV", 2003, 1, "Stage III Presentation", 4.0f);
                boolean result573 = this.myDB.InsertData("M.Tech", "AUTOMOBILE", "IV", 2003, 2, "Presentation and Final Viva Voce", 12.0f);
                return;
            case 2010:
                boolean result574 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 1, "Computational Methods", 4.0f);
                boolean result575 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 2, "Robotics", 4.0f);
                boolean result576 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 3, "Computer Aided Design", 3.0f);
                boolean result577 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 4, "Computer Aided Design Lab", 1.0f);
                boolean result578 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 5, "Automation and Mechatronics", 3.0f);
                boolean result579 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 6, "Automation and Mechatronics Lab", 1.0f);
                boolean result580 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 7, "Elective I", 4.0f);
                boolean result581 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 8, "Elective II", 3.0f);
                boolean result582 = this.myDB.InsertData("M.Tech", "CAD/CAM", "I", 2010, 9, "Elective II Lab", 1.0f);
                return;
            case 2011:
                boolean result583 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 1, "Research Methodology", 4.0f);
                boolean result584 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 2, "Advanced Manufacturing Processes", 4.0f);
                boolean result585 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 3, "Computer Integrated Manufacturing", 3.0f);
                boolean result586 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 4, "Computer Integrated Manufacturing Lab", 1.0f);
                boolean result587 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 5, "Finite Element Methods", 3.0f);
                boolean result588 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 6, "Finite Element Methods Lab", 1.0f);
                boolean result589 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 7, "Elective III", 3.0f);
                boolean result590 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 8, "Elective IV", 3.0f);
                boolean result591 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 9, "Elective IV Lab", 1.0f);
                boolean result592 = this.myDB.InsertData("M.Tech", "CAD/CAM", "II", 2011, 10, "Technical Seminar", 2.0f);
                return;
            case 2020:
                boolean result593 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 1, "Computational Methods", 4.0f);
                boolean result594 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 2, "Tribology", 4.0f);
                boolean result595 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 3, "Machine Dynamics and Vibration", 3.0f);
                boolean result596 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 4, "Machine Dynamics and Vibration Lab", 1.0f);
                boolean result597 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 5, "Theory of Elasticity, Plasticity and Material Behaviour", 3.0f);
                boolean result598 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 6, "Theory of Elasticity, Plasticity and Material Behaviour Lab", 1.0f);
                boolean result599 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 7, "Elective I", 4.0f);
                boolean result600 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 8, "Elective II", 3.0f);
                boolean result601 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "I", 2020, 9, "Elective II Lab", 1.0f);
                return;
            case 2021:
                boolean result602 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 1, "Research Methodology", 4.0f);
                boolean result603 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 2, "Mechanical Transmission Systems Design", 4.0f);
                boolean result604 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 3, "CAD/CAM", 3.0f);
                boolean result605 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 4, "CAD/CAM Lab", 1.0f);
                boolean result606 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 5, "Finite Element Methods", 3.0f);
                boolean result607 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 6, "Finite Element Methods Lab", 1.0f);
                boolean result608 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 7, "Elective II", 3.0f);
                boolean result609 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 8, "Elective III", 3.0f);
                boolean result610 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 9, "Elective III Lab", 1.0f);
                boolean result611 = this.myDB.InsertData("M.Tech", "MACHINE DESIGN", "II", 2021, 10, "Technical Seminar", 2.0f);
                return;
            case 2030:
                boolean result612 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 1, "Computational Methods", 4.0f);
                boolean result613 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 2, "Advanced Thermodynamics", 4.0f);
                boolean result614 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 3, "Advanced Fluid Dynamics", 3.0f);
                boolean result615 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 4, "Advanced Fluid Dynamics Lab", 1.0f);
                boolean result616 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 5, "Conduction and Radiation", 3.0f);
                boolean result617 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 6, "Conduction and Radiation Lab", 1.0f);
                boolean result618 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 7, "Elective I", 4.0f);
                boolean result619 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 8, "Elective II", 3.0f);
                boolean result620 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "I", 2030, 9, "Elective II Lab", 1.0f);
                return;
            case 2031:
                boolean result621 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 1, "Research Methodology", 4.0f);
                boolean result622 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 2, "Design of Heat Exchange Equipments", 4.0f);
                boolean result623 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 3, "Convective Heat Flow and Microfluidics", 3.0f);
                boolean result624 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 4, "Convective Heat Flow and Microfluidics Lab", 1.0f);
                boolean result625 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 5, "Computational Fluid Dynamics", 3.0f);
                boolean result626 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 6, "Computational Fluid Dynamics Lab", 1.0f);
                boolean result627 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 7, "Elective II", 3.0f);
                boolean result628 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 8, "Elective III", 3.0f);
                boolean result629 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 9, "Elective III Lab", 1.0f);
                boolean result630 = this.myDB.InsertData("M.Tech", "THERMAL ENGINEERING", "II", 2031, 10, "Technical Seminar", 2.0f);
                return;
            case 2040:
                boolean result631 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 1, "Computational Methods", 4.0f);
                boolean result632 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 2, "Management Principles and Practice", 4.0f);
                boolean result633 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 3, "Advanced Materials and Construction Techniques", 3.0f);
                boolean result634 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 4, "Advanced Project Management", 3.0f);
                boolean result635 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 5, "Elective I", 4.0f);
                boolean result636 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 6, "Elective II", 3.0f);
                boolean result637 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 7, "Sampling and Testing of Materials", 1.0f);
                boolean result638 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 8, "Planning and Scheduling", 1.0f);
                boolean result639 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "I", 2040, 9, "Quantity Survey and Estimation", 1.0f);
                return;
            case 2041:
                boolean result640 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 1, "Research Methodology", 4.0f);
                boolean result641 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 2, "Construction Resource Management", 4.0f);
                boolean result642 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 3, "Construction Contract Management", 3.0f);
                boolean result643 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 4, "Construction Cost Engineering", 3.0f);
                boolean result644 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 5, "Elective III", 3.0f);
                boolean result645 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 6, "Elective IV", 3.0f);
                boolean result646 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 7, "Cost Engineering", 1.0f);
                boolean result647 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 8, "Construction Project Management", 1.0f);
                boolean result648 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 9, "Building Information Modelling", 1.0f);
                boolean result649 = this.myDB.InsertData("M.Tech", "CONSTRUCTION MANAGEMENT", "II", 2041, 10, "Technical Seminar", 2.0f);
                return;
            case 2050:
                boolean result650 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 1, "Computational Methods", 4.0f);
                boolean result651 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 2, "Environmental Chemistry", 4.0f);
                boolean result652 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 3, "Environmental Microbiology", 3.0f);
                boolean result653 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 4, "Hydraulics of Water and WasteWater", 3.0f);
                boolean result654 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 5, "Elective I", 4.0f);
                boolean result655 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 6, "Elective II", 3.0f);
                boolean result656 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 7, "Solid and Microbiology Lab", 1.0f);
                boolean result657 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 8, "Water and Waste water Lab", 1.0f);
                boolean result658 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "I", 2050, 9, "Air and Noise Pollution Control Lab", 1.0f);
                return;
            case 2051:
                boolean result659 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 1, "Research Methodology", 4.0f);
                boolean result660 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 2, "Design of Water Treatment System", 3.0f);
                boolean result661 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 3, "Design of Waste Water Treatment System", 4.0f);
                boolean result662 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 4, "Solid and Hazardous Waste Management", 3.0f);
                boolean result663 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 5, "Elective III", 3.0f);
                boolean result664 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 6, "Elective IV", 3.0f);
                boolean result665 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 7, "Water and Waste Water Lab", 1.0f);
                boolean result666 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 8, "Computer Application Lab", 1.0f);
                boolean result667 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 9, "Project Implementation Lab", 1.0f);
                boolean result668 = this.myDB.InsertData("M.Tech", "ENVIRONMENTAL", "II", 2051, 10, "Technical Seminar", 2.0f);
                return;
            case 2060:
                boolean result669 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 1, "Computational Methods", 4.0f);
                boolean result670 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 2, "Continuum Solid Mechanics", 3.0f);
                boolean result671 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 3, "Experimental Methods in Structural Engineering", 3.0f);
                boolean result672 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 4, "Advanced Geotechnical Engineering", 3.0f);
                boolean result673 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 5, "Elective I", 4.0f);
                boolean result674 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 6, "Elective II", 4.0f);
                boolean result675 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 7, "Experimental Methods Lab", 1.5f);
                boolean result676 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "I", 2060, 8, "Numerical Methods Lab", 1.5f);
                return;
            case 2061:
                boolean result677 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 1, "Research Methodology", 4.0f);
                boolean result678 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 2, "Finite Element Method", 3.0f);
                boolean result679 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 3, "Design of Pre-Stressed Concrete Structures", 3.0f);
                boolean result680 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 4, "Design of Concrete Infrastructural and Industrial Structures", 3.0f);
                boolean result681 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 5, "Elective III", 4.0f);
                boolean result682 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 6, "Elective IV", 3.0f);
                boolean result683 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 7, "Advanced Computer Aided Analysis Lab", 1.5f);
                boolean result684 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 8, "Advanced Computer Aided Design Lab", 1.5f);
                boolean result685 = this.myDB.InsertData("M.Tech", "STRUCTURAL", "II", 2061, 9, "Technical Seminar", 2.0f);
                return;
            case 2070:
                boolean result686 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 1, "Computational Methods", 4.0f);
                boolean result687 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 2, "Optimal Control", 4.0f);
                boolean result688 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 3, "Dynamical Systems", 3.0f);
                boolean result689 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 4, "Dynamical Systems Lab", 1.0f);
                boolean result690 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 5, "Linear Control Design", 3.0f);
                boolean result691 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 6, "Linear Control Design Lab", 1.0f);
                boolean result692 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 7, "Elective I", 4.0f);
                boolean result693 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 8, "Elective II", 3.0f);
                boolean result694 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "I", 2070, 9, "Elective II Lab", 1.0f);
                return;
            case 2071:
                boolean result695 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 1, "Research Methodology", 4.0f);
                boolean result696 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 2, "Non Linear System Analysis", 4.0f);
                boolean result697 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 3, "Non Linear Control Design", 3.0f);
                boolean result698 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 4, "Non Linear Control Design Lab", 1.0f);
                boolean result699 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 5, "System Identification,Estimation and Filtering", 3.0f);
                boolean result700 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 6, "System Identification,Estimation and Filtering Lab", 1.0f);
                boolean result701 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 7, "Elective II", 3.0f);
                boolean result702 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 8, "Elective III", 3.0f);
                boolean result703 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 9, "Elective III Lab", 1.0f);
                boolean result704 = this.myDB.InsertData("M.Tech", "CONTROL SYSTEMS", "II", 2071, 10, "Technical Seminar", 2.0f);
                return;
            case 2080:
                boolean result705 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 1, "Computational Methods", 4.0f);
                boolean result706 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 2, "Power System Protection and Relaying", 4.0f);
                boolean result707 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 3, "Advanced Power Systems", 3.0f);
                boolean result708 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 4, "Advanced Power Systems Lab", 1.0f);
                boolean result709 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 5, "Linear Control Design", 3.0f);
                boolean result710 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 6, "Linear Control Design Lab", 1.0f);
                boolean result711 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 7, "Elective I", 4.0f);
                boolean result712 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 8, "Elective II", 3.0f);
                boolean result713 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "I", 2080, 9, "Elective II Lab", 1.0f);
                return;
            case 2081:
                boolean result714 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 1, "Research Methodology", 4.0f);
                boolean result715 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 2, "Restructured Power System", 4.0f);
                boolean result716 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 3, "Power System Stability", 3.0f);
                boolean result717 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 4, "Power System Stability Lab", 1.0f);
                boolean result718 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 5, "System Identification, Estimation and Filtering", 3.0f);
                boolean result719 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 6, "System Identification, Estimation and Filtering Lab", 1.0f);
                boolean result720 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 7, "Elective III", 3.0f);
                boolean result721 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 8, "Elective IV", 3.0f);
                boolean result722 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 9, "Elective IV Lab", 1.0f);
                boolean result723 = this.myDB.InsertData("M.Tech", "POWER SYSTEMS", "II", 2081, 10, "Technical Seminar", 2.0f);
                return;
            case 2090:
                boolean result724 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 1, "Computational Methods", 4.0f);
                boolean result725 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 2, "Advanced Communication Theory", 4.0f);
                boolean result726 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 3, "VLSI System Design", 3.0f);
                boolean result727 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 4, "VLSI System Design Lab", 1.0f);
                boolean result728 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 5, "Digital System Design", 3.0f);
                boolean result729 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 6, "Digital System Design Lab", 1.0f);
                boolean result730 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 7, "Elective I", 4.0f);
                boolean result731 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 8, "Elective II", 3.0f);
                boolean result732 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "I", 2090, 9, "Elective II Lab", 1.0f);
                return;
            case 2091:
                boolean result733 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 1, "Research Methodology", 4.0f);
                boolean result734 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 2, "Advanced DSP", 4.0f);
                boolean result735 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 3, "Nano Electronix", 3.0f);
                boolean result736 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 4, "Nano Electronix Lab", 1.0f);
                boolean result737 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 5, "Embedded System Design", 3.0f);
                boolean result738 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 6, "Embedded System Design Lab", 1.0f);
                boolean result739 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 7, "Elective III", 3.0f);
                boolean result740 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 8, "Elective IV", 3.0f);
                boolean result741 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 9, "Elective IV Lab", 1.0f);
                boolean result742 = this.myDB.InsertData("M.Tech", "ELECTRONIX", "II", 2091, 10, "Technical Seminar", 2.0f);
                return;
            case 2100:
                boolean result743 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 1, "Computational Methods", 4.0f);
                boolean result744 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 2, "Statistical Theory of Communication", 4.0f);
                boolean result745 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 3, "RF Integrated Circuits", 3.0f);
                boolean result746 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 4, "RF Integrated Circuits Lab", 1.0f);
                boolean result747 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 5, "Modern Communication Networks", 3.0f);
                boolean result748 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 6, "Modern Communication Networks Lab", 1.0f);
                boolean result749 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 7, "Elective I", 4.0f);
                boolean result750 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 8, "Elective II", 3.0f);
                boolean result751 = this.myDB.InsertData("M.Tech", "EXTC", "I", 2100, 9, "Elective II Lab", 1.0f);
                return;
            case 2101:
                boolean result752 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 1, "Research Methodology", 4.0f);
                boolean result753 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 2, "Error Correcting Codes", 4.0f);
                boolean result754 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 3, "Advanced Mobile Communication", 3.0f);
                boolean result755 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 4, "Advanced Mobile Communication Lab", 1.0f);
                boolean result756 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 5, "Embedded System Design", 3.0f);
                boolean result757 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 6, "Embedded System Design Lab", 1.0f);
                boolean result758 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 7, "Elective III", 3.0f);
                boolean result759 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 8, "Elective IV", 3.0f);
                boolean result760 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 9, "Elective IV Lab", 1.0f);
                boolean result761 = this.myDB.InsertData("M.Tech", "EXTC", "II", 2101, 10, "Technical Seminar", 2.0f);
                return;
            case 2110:
                boolean result762 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 1, "Computational Methods", 4.0f);
                boolean result763 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 2, "Cloud Architecture Infrastructure and Technology", 4.0f);
                boolean result764 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 3, "TCP/IP and Network Programming", 3.0f);
                boolean result765 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 4, "TCP/IP and Network Programming Lab", 1.0f);
                boolean result766 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 5, "Modern Information System", 3.0f);
                boolean result767 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 6, "Modern Information System Lab", 1.0f);
                boolean result768 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 7, "Elective I", 4.0f);
                boolean result769 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 8, "Elective II", 3.0f);
                boolean result770 = this.myDB.InsertData("M.Tech", "NIMS", "I", 2110, 9, "Elective II Lab", 1.0f);
                return;
            case 2111:
                boolean result771 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 1, "Research Methodology", 4.0f);
                boolean result772 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 2, "Network Attacks and Defence Mechanism", 4.0f);
                boolean result773 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 3, "Advanced Database Management System", 3.0f);
                boolean result774 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 4, "Advanced Database Management System Lab", 1.0f);
                boolean result775 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 5, "Network Design and Administration", 3.0f);
                boolean result776 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 6, "Network Design and Administration Lab", 1.0f);
                boolean result777 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 7, "Elective III", 3.0f);
                boolean result778 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 8, "Elective IV", 3.0f);
                boolean result779 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 9, "Elective IV Lab", 1.0f);
                boolean result780 = this.myDB.InsertData("M.Tech", "NIMS", "II", 2111, 10, "Technical Seminar", 2.0f);
                return;
            case 2120:
                boolean result781 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 1, "Computational Methods", 4.0f);
                boolean result782 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 2, "Advanced Compiler", 4.0f);
                boolean result783 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 3, "TCP/IP and Network Programming", 3.0f);
                boolean result784 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 4, "TCP/IP and Network Programming Lab", 1.0f);
                boolean result785 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 5, "Modern Information Systems", 3.0f);
                boolean result786 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 6, "Modern Information Systems Lab", 1.0f);
                boolean result787 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 7, "Elective I", 4.0f);
                boolean result788 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 8, "Elective II", 3.0f);
                boolean result789 = this.myDB.InsertData("M.Tech", "COMPUTER", "I", 2120, 9, "Elective II Lab", 1.0f);
                return;
            case 2121:
                boolean result790 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 1, "Research Methodologies", 4.0f);
                boolean result791 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 2, "Cloud Architecture Infrastruture and Technology", 4.0f);
                boolean result792 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 3, "Advanced Database Management Systems", 3.0f);
                boolean result793 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 4, "Advanced Database Management Systems Lab", 1.0f);
                boolean result794 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 5, "Information Security", 3.0f);
                boolean result795 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 6, "Information Security Lab", 1.0f);
                boolean result796 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 7, "Elective III", 3.0f);
                boolean result797 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 8, "Elective IV", 3.0f);
                boolean result798 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 9, "Elective IV Lab", 1.0f);
                boolean result799 = this.myDB.InsertData("M.Tech", "COMPUTER", "II", 2121, 10, "Technical Seminar", 2.0f);
                return;
            case 2130:
                boolean result800 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 1, "Computational Methods", 4.0f);
                boolean result801 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 2, "Software Architecture and Design Pattern", 4.0f);
                boolean result802 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 3, "TCP/IP and Network Programming", 3.0f);
                boolean result803 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 4, "TCP/IP and Network Programming Lab", 1.0f);
                boolean result804 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 5, "Advanced Software Engineering", 3.0f);
                boolean result805 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 6, "Advanced Software Engineering Lab", 1.0f);
                boolean result806 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 7, "Elective I", 4.0f);
                boolean result807 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 8, "Elective II", 3.0f);
                boolean result808 = this.myDB.InsertData("M.Tech", "SOFTWARE", "I", 2130, 9, "Elective II Lab", 1.0f);
                return;
            case 2131:
                boolean result809 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 1, "Research Methodology", 4.0f);
                boolean result810 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 2, "Service Oriented Architecture", 4.0f);
                boolean result811 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 3, "Advanced Database Management Systems", 3.0f);
                boolean result812 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 4, "Advanced Database Management Systems Lab", 1.0f);
                boolean result813 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 5, "Information Security", 3.0f);
                boolean result814 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 6, "Information Security Lab", 1.0f);
                boolean result815 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 7, "Elective III", 3.0f);
                boolean result816 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 8, "Elective IV", 3.0f);
                boolean result817 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 9, "Elective IV Lab", 1.0f);
                boolean result818 = this.myDB.InsertData("M.Tech", "SOFTWARE", "II", 2131, 10, "Technical Seminar", 2.0f);
                return;
            case 2160:
                boolean result819 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 1, "Computational Methods", 4.0f);
                boolean result820 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 2, "High Tech Fibres", 4.0f);
                boolean result821 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 3, "Characterization and Properties of Textile Materials", 3.0f);
                boolean result822 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 4, "Characterization and Properties of Textile Materials Lab", 1.0f);
                boolean result823 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 5, "Advances in Textile Technology", 3.0f);
                boolean result824 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 6, "Advances in Textile Technology Lab", 1.0f);
                boolean result825 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 7, "Elective I", 4.0f);
                boolean result826 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 8, "Elective II", 3.0f);
                boolean result827 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "I", 2160, 9, "Elective II Lab", 1.0f);
                return;
            case 2161:
                boolean result828 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 1, "Research Methodology", 4.0f);
                boolean result829 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 2, "Management of Textile Industry", 4.0f);
                boolean result830 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 3, "Technical Textiles", 3.0f);
                boolean result831 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 4, "Technical Textiles Lab", 1.0f);
                boolean result832 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 5, "Advanced Textile Composites", 3.0f);
                boolean result833 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 6, "Advanced Textile Composites Lab", 1.0f);
                boolean result834 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 7, "Elective III", 3.0f);
                boolean result835 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 8, "Elective IV", 3.0f);
                boolean result836 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 9, "Elective IV Lab", 1.0f);
                boolean result837 = this.myDB.InsertData("M.Tech", "TEXTILE TECHNOLOGY", "II", 2161, 10, "Technical Seminar", 2.0f);
                return;
            case PathInterpolatorCompat.MAX_NUM_POINTS:
                boolean result838 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 1, "Discrete Mathematics", 5.0f);
                boolean result839 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 2, "C++ Programming", 3.0f);
                boolean result840 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 3, "C++ Programming Lab", 1.0f);
                boolean result841 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 4, "Structured System Analysis and Design", 3.0f);
                boolean result842 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 5, "Structured System Analysis and Design Lab", 1.0f);
                boolean result843 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 6, "Operating Systems", 3.0f);
                boolean result844 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 7, "Operating Systems Lab", 1.0f);
                boolean result845 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 8, "Computer Organization and Architecture", 3.0f);
                boolean result846 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 9, "Computer Organization and Architecture Lab", 1.0f);
                boolean result847 = this.myDB.InsertData("MCA", "NA", "I", PathInterpolatorCompat.MAX_NUM_POINTS, 10, "Accounting and Finance", 4.0f);
                return;
            case 3001:
                boolean result848 = this.myDB.InsertData("MCA", "NA", "II", 3001, 1, "Introduction to Statistics", 5.0f);
                boolean result849 = this.myDB.InsertData("MCA", "NA", "II", 3001, 2, "Java Programming", 3.0f);
                boolean result850 = this.myDB.InsertData("MCA", "NA", "II", 3001, 3, "Java Programming Lab", 1.0f);
                boolean result851 = this.myDB.InsertData("MCA", "NA", "II", 3001, 4, "Web Technology", 3.0f);
                boolean result852 = this.myDB.InsertData("MCA", "NA", "II", 3001, 5, "Web Technology Lab", 1.0f);
                boolean result853 = this.myDB.InsertData("MCA", "NA", "II", 3001, 6, "Data and File Structures", 3.0f);
                boolean result854 = this.myDB.InsertData("MCA", "NA", "II", 3001, 7, "Data and File Structures Lab", 1.0f);
                boolean result855 = this.myDB.InsertData("MCA", "NA", "II", 3001, 8, "Object Oriented Analysis Design", 3.0f);
                boolean result856 = this.myDB.InsertData("MCA", "NA", "II", 3001, 9, "Object Oriented Analysis Design Lab", 1.0f);
                boolean result857 = this.myDB.InsertData("MCA", "NA", "II", 3001, 10, "Data Communication Network", 3.0f);
                boolean result858 = this.myDB.InsertData("MCA", "NA", "II", 3001, 10, "Data Communication Network Lab", 1.0f);
                return;
            case 3002:
                boolean result859 = this.myDB.InsertData("MCA", "NA", "III", 3002, 1, "Database Management Systems", 3.0f);
                boolean result860 = this.myDB.InsertData("MCA", "NA", "III", 3002, 2, "Database Management Systems Lab", 1.0f);
                boolean result861 = this.myDB.InsertData("MCA", "NA", "III", 3002, 3, "Human Computer Interaction", 3.0f);
                boolean result862 = this.myDB.InsertData("MCA", "NA", "III", 3002, 4, "Human Computer Interaction Lab", 1.0f);
                boolean result863 = this.myDB.InsertData("MCA", "NA", "III", 3002, 5, "Mobile Computing", 3.0f);
                boolean result864 = this.myDB.InsertData("MCA", "NA", "III", 3002, 6, "Mobile Computing Lab", 1.0f);
                boolean result865 = this.myDB.InsertData("MCA", "NA", "III", 3002, 7, "Principles of Management", 4.0f);
                boolean result866 = this.myDB.InsertData("MCA", "NA", "III", 3002, 8, "Personality Skills and Communication", 4.0f);
                boolean result867 = this.myDB.InsertData("MCA", "NA", "III", 3002, 9, "Unix Programming Lab", 1.0f);
                boolean result868 = this.myDB.InsertData("MCA", "NA", "III", 3002, 10, "Elective-I", 4.0f);
                return;
            case 3003:
                boolean result869 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 1, "Data Warehousing and Mining", 3.0f);
                boolean result870 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 2, "Data Warehousing and Mining Lab", 1.0f);
                boolean result871 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 3, "Information and Network Security", 3.0f);
                boolean result872 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 4, "Information and Network Security Lab", 1.0f);
                boolean result873 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 5, "Optimization Techniques", 5.0f);
                boolean result874 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 6, "Management Information Systems", 4.0f);
                boolean result875 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 7, "Emerging Technologies Lab-I", 1.0f);
                boolean result876 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 8, "Elective-II", 3.0f);
                boolean result877 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 9, "Elective-II Lab", 1.0f);
                boolean result878 = this.myDB.InsertData("MCA", "NA", "IV", 3003, 10, "Elective-III", 4.0f);
                return;
            case 3004:
                boolean result879 = this.myDB.InsertData("MCA", "NA", "V", 3004, 1, "Distributed System", 3.0f);
                boolean result880 = this.myDB.InsertData("MCA", "NA", "V", 3004, 2, "Distributed System Lab", 1.0f);
                boolean result881 = this.myDB.InsertData("MCA", "NA", "V", 3004, 3, "Business Analytics and Business Intelligence", 3.0f);
                boolean result882 = this.myDB.InsertData("MCA", "NA", "V", 3004, 4, "Business Analytics and Business Intelligence Lab", 1.0f);
                boolean result883 = this.myDB.InsertData("MCA", "NA", "V", 3004, 5, "Cloud Computing", 4.0f);
                boolean result884 = this.myDB.InsertData("MCA", "NA", "V", 3004, 6, "Software Engineering and Testing", 4.0f);
                boolean result885 = this.myDB.InsertData("MCA", "NA", "V", 3004, 7, "Project Management", 4.0f);
                boolean result886 = this.myDB.InsertData("MCA", "NA", "V", 3004, 8, "Emerging Technologies Lab-II", 1.0f);
                boolean result887 = this.myDB.InsertData("MCA", "NA", "V", 3004, 9, "Advanced Algorithms Lab", 1.0f);
                boolean result888 = this.myDB.InsertData("MCA", "NA", "V", 3004, 10, "Technical Seminar", 3.0f);
                return;
            case 3005:
                boolean result889 = this.myDB.InsertData("MCA", "NA", "VI", 3005, 1, "Stage-I Presentation", 4.0f);
                boolean result890 = this.myDB.InsertData("MCA", "NA", "VI", 3005, 2, "Stage-II Presentation", 4.0f);
                boolean result891 = this.myDB.InsertData("MCA", "NA", "VI", 3005, 3, "Final Presentation and Viva Voce", 12.0f);
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id2) {
        this.pointers[Integer.parseInt("" + ((Spinner) parent).getTag())] = (float) this.items[position].intValue();
        this.summarks = 0.0f;
        for (int i = 0; i < this.count; i++) {
            this.summarks += this.credits[i] * this.pointers[i];
        }
        this.spi = this.summarks / this.sumcredits;
        this.spiText.setText("" + String.format("%.2f", new Object[]{Float.valueOf(this.spi)}));
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
