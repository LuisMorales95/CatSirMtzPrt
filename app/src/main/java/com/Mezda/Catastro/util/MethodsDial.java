package com.Mezda.Catastro.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.Mezda.Catastro.R;
import com.Mezda.Catastro.fragment.DetailViewFragment;

/**
 * Created by Black Swan on 22/02/2017.
 */

public class MethodsDial {


    public static void showSimpleDialog(final Activity context, final String heading, final String body, final String positiveString, final String negativeString) {
        AlertDialog dialog = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        ((TextView) view.findViewById(R.id.bodyTV)).setText(body);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //phoneCall(context, FINDER_HOTLINE);

                    }
                })
                .setNegativeButton(negativeString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
/*
    public static void showCvvDialog(final Context context, final String heading, final String positiveString) {
        AlertDialog dialog = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_cvc_help, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setNegativeButton(positiveString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })


                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public static void showAddMonDialog(final Activity context, final String heading, final TextView tvc) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_add_money, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        //final EditText et = (EditText) view.findViewById(R.id.CantField);

        //et.setText(FragTiendasConv.cantidad);

        final Spinner spinner2;
        spinner2 = (Spinner) view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("$50");
        list.add("$100");
        list.add("$200");
        list.add("$300");
        list.add("$400");
        list.add("$500");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        //spinner2.getSelectedItem().toString().replace("$","");
        if(ActStores.cantidad.equals("50")){
            spinner2.setSelection(0);
        }else if(ActStores.cantidad.equals("100")){
            spinner2.setSelection(1);
        }else if(ActStores.cantidad.equals("200")){
            spinner2.setSelection(2);
        }else if(ActStores.cantidad.equals("300")){
            spinner2.setSelection(3);
        }else if(ActStores.cantidad.equals("400")){
            spinner2.setSelection(4);
        }else if(ActStores.cantidad.equals("500")){
            spinner2.setSelection(5);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ActStores.cantidad = spinner2.getSelectedItem().toString().replace("$","");
                tvc.setText(spinner2.getSelectedItem().toString() +".00 MXN");
                finalDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    }

    public static void showDeleteTarjetDialog(final Activity context, final String heading, final String positiveString, final String negativeString) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_delete_tarjet, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                FragmentTarjetas.KillCard(FragmentTarjetas.getIdItem());
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finalDialog.dismiss();
            }
        });

    }


    public static void showPaymentTarjetDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_f_payment, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        final RadioButton a = (RadioButton) view.findViewById(R.id.ck_efect);
        final RadioButton b = (RadioButton) view.findViewById(R.id.ck_saldo);
        final RadioButton c = (RadioButton) view.findViewById(R.id.ck_tarj);
        final ImageView ivc = (ImageView) view.findViewById(R.id.img_conf_tar);
        final ImageView ivs = (ImageView) view.findViewById(R.id.img_sldd);
        final ImageView ive = (ImageView) view.findViewById(R.id.img_efecc);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ok_buttond);

        RelativeLayout r0 = (RelativeLayout) view.findViewById(R.id.refer_container0);
        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.refer_container1);
        RelativeLayout r2 = (RelativeLayout) view.findViewById(R.id.refer_container2);


        if(User.FormaPago ==1){
            a.setChecked(true);
        }else if(User.FormaPago ==2){
            b.setChecked(true);
        }else if(User.FormaPago ==3){
            c.setChecked(true);
            ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.bluee_seo_cogwheels_setting));
        }



        r0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setChecked(true);
                a.setChecked(false);
                b.setChecked(false);
                ConfirmRequest.pn.setText(c.getText());

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
                ivc.startAnimation(animation);
                User.FormaPago = 3;
                ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.bluee_seo_cogwheels_setting));
                //ivs.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile_balance));
                //ive.setImageDrawable(context.getResources().getDrawable(R.drawable.black_money));
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.Saldo <=0){
                    b.setChecked(false);
                }else{
                    b.setChecked(true);
                    a.setChecked(false);
                    c.setChecked(false);
                    ConfirmRequest.pn.setText(b.getText());
                    User.FormaPago = 2;
                    ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.seo_cogwheels_setting));
                    //ivs.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_mobile_balance));
                    //ive.setImageDrawable(context.getResources().getDrawable(R.drawable.black_money));
                }
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);
                c.setChecked(false);
                ConfirmRequest.pn.setText(a.getText());
                User.FormaPago = 1;
                ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.seo_cogwheels_setting));
                //ivs.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile_balance));
                //ive.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_money));
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);
                c.setChecked(false);
                ConfirmRequest.pn.setText(a.getText());
                User.FormaPago = 1;
                ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.seo_cogwheels_setting));
                //ivs.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile_balance));
                //ive.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_money));
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.Saldo <=0){
                    b.setChecked(false);
                }else{
                b.setChecked(true);
                a.setChecked(false);
                c.setChecked(false);
                    ConfirmRequest.pn.setText(b.getText());
                    User.FormaPago = 2;
                    ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.seo_cogwheels_setting));
                    //ivs.setImageDrawable(context.getResources().getDrawable(R.drawable.blue_mobile_balance));
                    //ive.setImageDrawable(context.getResources().getDrawable(R.drawable.black_money));
                }
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setChecked(true);
                a.setChecked(false);
                b.setChecked(false);
                ConfirmRequest.pn.setText(c.getText());
                User.FormaPago = 3;

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.shake);
                ivc.startAnimation(animation);

                ivc.setImageDrawable(context.getResources().getDrawable(R.drawable.bluee_seo_cogwheels_setting));
                //ivs.setImageDrawable(context.getResources().getDrawable(R.drawable.mobile_balance));
                //ive.setImageDrawable(context.getResources().getDrawable(R.drawable.black_money));
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(context)

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });


        ivc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (c.isChecked()){
                    context.startActivity(new Intent(context, TarjetasGuardadas.class));
                    finalDialog.dismiss();
                }

            }
        });
    }


    //*************************************** DESTINO
    public static void showSourceDestDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_dest_select, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        final RadioButton a = (RadioButton) view.findViewById(R.id.ck_efect);
        final RadioButton b = (RadioButton) view.findViewById(R.id.ck_saldo);
        final RadioButton c = (RadioButton) view.findViewById(R.id.ck_tarj);
        final ImageView ivc = (ImageView) view.findViewById(R.id.img_conf_tar);
        final ImageView ivs = (ImageView) view.findViewById(R.id.img_sldd);
        final ImageView ive = (ImageView) view.findViewById(R.id.img_efecc);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);

        RelativeLayout r0 = (RelativeLayout) view.findViewById(R.id.refer_container0);
        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.refer_container1);
        RelativeLayout r2 = (RelativeLayout) view.findViewById(R.id.refer_container2);


        a.setChecked(true);

        r0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setChecked(true);
                a.setChecked(false);
                b.setChecked(false);
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    b.setChecked(true);
                    a.setChecked(false);
                    c.setChecked(false);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);
                c.setChecked(false);
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);
                c.setChecked(false);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    b.setChecked(true);
                    a.setChecked(false);
                    c.setChecked(false);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setChecked(true);
                a.setChecked(false);
                b.setChecked(false);
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(context)

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();



        final AlertDialog finalDialog = dialog;

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    }

    //*************************************** ORIGEN
    public static void showSourceOrigenDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_origen_select, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        final RadioButton a = (RadioButton) view.findViewById(R.id.ck_efect);
        final RadioButton b = (RadioButton) view.findViewById(R.id.ck_saldo);
        final RadioButton c = (RadioButton) view.findViewById(R.id.ck_tarj);
        final ImageView ivc = (ImageView) view.findViewById(R.id.img_conf_tar);
        final ImageView ivs = (ImageView) view.findViewById(R.id.img_sldd);
        final ImageView ive = (ImageView) view.findViewById(R.id.img_efecc);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);

        RelativeLayout r0 = (RelativeLayout) view.findViewById(R.id.refer_container0);
        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.refer_container1);
        RelativeLayout r2 = (RelativeLayout) view.findViewById(R.id.refer_container2);


        a.setChecked(true);

        r0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setChecked(true);
                a.setChecked(false);
                b.setChecked(false);
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setChecked(true);
                a.setChecked(false);
                c.setChecked(false);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);
                c.setChecked(false);
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);
                c.setChecked(false);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setChecked(true);
                a.setChecked(false);
                c.setChecked(false);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.setChecked(true);
                a.setChecked(false);
                b.setChecked(false);
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(context)

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    }

    //*************************************** CONFIGURACION DE VEHICULO
    public static void showConfCarDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_conf_car, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        final RadioButton a = (RadioButton) view.findViewById(R.id.ck_efect);
        final RadioButton b = (RadioButton) view.findViewById(R.id.ck_saldo);

        final CheckBox c = (CheckBox) view.findViewById(R.id.ck_tarj);

        final ImageView ivc = (ImageView) view.findViewById(R.id.img_conf_tar);
        final ImageView ivs = (ImageView) view.findViewById(R.id.img_sldd);
        final ImageView ive = (ImageView) view.findViewById(R.id.img_efecc);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ok_buttond);

        RelativeLayout r0 = (RelativeLayout) view.findViewById(R.id.refer_container0);

        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.refer_container1);
        RelativeLayout r2 = (RelativeLayout) view.findViewById(R.id.refer_container2);


        a.setChecked(true);



        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setChecked(true);
                a.setChecked(false);

            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);

            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setChecked(true);
                b.setChecked(false);

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setChecked(true);
                a.setChecked(false);

            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(context)

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    } */


    public static void showNoteObs(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_note, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        final EditText s = (EditText) view.findViewById(R.id.etComment);
        s.setText(DetailViewFragment.Obs);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailViewFragment.Obs = s.getText().toString();
                finalDialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    }

    public static void showNoteUsoP(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_usop, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        final EditText s = (EditText) view.findViewById(R.id.etComment);
        s.setText(DetailViewFragment.Otrouso);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailViewFragment.Otrouso = s.getText().toString();
                finalDialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    }


    /*
    //*************************************** NOTA
    public static void showDriverOfferDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_driver_offer, null);
        final CircularProgressBar circularProgressBar3 = (CircularProgressBar) view.findViewById(R.id.circularProgress);

        circularProgressBar3.setProgressColor(view.getResources().getColor(R.color.black_semi_transparent));
        circularProgressBar3.setProgressWidth(5);//ANCHO
        circularProgressBar3.showProgressText(false);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);

        final TextView tim= (TextView) view.findViewById(R.id.time_v);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();


        final boolean[] enProgreso = {true};
        final Handler handler = new Handler();
        final int[] i = {0};

        final AlertDialog finalDialog = dialog;
        circularProgressBar3.setProgress(100);

        Runnable tarea = new Runnable() {
            @Override
            public void run() {
                 int z = i[0]+1;
                 i[0] = z;

                int porc = Integer.parseInt((100-((z * 100)/30)) +"");
                circularProgressBar3.setProgress(porc);

                tim.setText((30-z) +"");

                 if(z==30){
                     enProgreso[0] = false;
                     finalDialog.dismiss();
                 }

                if (enProgreso[0]){
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(tarea, 1000);

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                enProgreso[0] = false;
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                enProgreso[0] = false;
            }
        });

    } */

    /*
    //*************************************** TARIFA
    public static void showTarifaDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final FloatLabel etarif;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_tarifa, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);

        etarif = (FloatLabel) view.findViewById(R.id.etctarifa);
        etarif.getEditText().addTextChangedListener(new NumberTextWatcherFloat(etarif, "#,###"));

        //
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);
        dialog = builder.create();
        dialog.show();




        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn = etarif.getEditText().getText().toString();
                ConfirmRequest.tarif.setText(!fn.equals("")? fn : "$0.00");
                String num = etarif.getEditText().getText().toString().replace("$","").replace(".00","");
                ConfirmRequest.conf.put("Tarifa", ""+Integer.parseInt((!num.equals("")? num : "0")));
                finalDialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    } */



/*
    //***************************************EXTRAS
    public static void showExtrasDialog(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_extras, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);
        final CheckBox a = (CheckBox) view.findViewById(R.id.ck_efect);
        final CheckBox b = (CheckBox) view.findViewById(R.id.ck_saldo);
        final CheckBox c = (CheckBox) view.findViewById(R.id.ck_tarj);
        final ImageView ivc = (ImageView) view.findViewById(R.id.img_conf_tar);
        final ImageView ivs = (ImageView) view.findViewById(R.id.img_sldd);
        final ImageView ive = (ImageView) view.findViewById(R.id.img_efecc);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);

        RelativeLayout r0 = (RelativeLayout) view.findViewById(R.id.refer_container0);
        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.refer_container1);
        RelativeLayout r2 = (RelativeLayout) view.findViewById(R.id.refer_container2);




        r0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(c.isChecked()){
                    c.setChecked(false);
                }else{
                    c.setChecked(true);
                }
                //a.setChecked(false);
                //b.setChecked(false);
            }
        });

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b.isChecked()){
                    b.setChecked(false);
                }else{
                    b.setChecked(true);
                }
                //a.setChecked(false);
                //c.setChecked(false);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a.isChecked()){
                    a.setChecked(false);
                }else{
                    a.setChecked(true);
                }
                //b.setChecked(false);
                //c.setChecked(false);
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //a.setChecked(true);
                //b.setChecked(false);
                //c.setChecked(false);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // b.setChecked(true);
                //a.setChecked(false);
                //c.setChecked(false);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //c.setChecked(true);
                //a.setChecked(false);
                //b.setChecked(false);
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(context)

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });

    } */
}
