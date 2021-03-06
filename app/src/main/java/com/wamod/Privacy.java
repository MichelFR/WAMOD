package com.wamod;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.whatsapp.ContactInfo;
import com.whatsapp.GroupChatInfo;
import com.whatsapp.protocol.a;
import com.whatsapp.protocol.g;

/**
 * Created by brianvalente on 5/8/16.
 */
public class Privacy {
    static boolean stringsDecoded = false;

    public static boolean blueTick(a a, g g, String str1, String str2, String[] str3, String str4) {
        // Returns TRUE if the "Hide blue tick" option is DISABLED
        log(g, str1, str2, str3, str4);
        String JabberID = g.a;
        if (!reportReadForSpecificContact(JabberID)) {
            str1 = str4;
            if (reportReceivedForSpecificContact(JabberID)) a.a(g, str1, str2, str3, str4);
            return false;
        } else return true;
    }

    public static boolean hideTyping(String JabberID) {
        // Returns TRUE if the "Hide typing" option is DISABLED
        return reportTypingForSpecificContact(JabberID);
    }

    public static boolean secondTick(g g) {
        // Returns TRUE if the "Hide second tick" option is DISABLED
        String JabberID = g.a;
        return reportReceivedForSpecificContact(JabberID);
    }

    public static boolean generalReportReceived() {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean("general_reportreceived", true);
        return bool;
    }

    public static boolean generalReportRead() {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean("general_reportread", true);
        return bool;
    }

    public static boolean generalReportTyping() {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean("general_reporttyping", true);
        return bool;
    }



    public static boolean customPrivacyForSpecificContact(String JabberID) {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean(JabberID, false);
        return bool;
    }



    public static boolean reportReadForSpecificContact(String JabberID) {
        if (!customPrivacyForSpecificContact(JabberID)) return generalReportRead();
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean(JabberID + "_reportread", true);
        return bool;
    }

    public static boolean reportReceivedForSpecificContact(String JabberID) {
        if (!customPrivacyForSpecificContact(JabberID)) return generalReportReceived();
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean(JabberID + "_reportreceived", true);
        return bool;
    }

    public static boolean reportTypingForSpecificContact(String JabberID) {
        if (!customPrivacyForSpecificContact(JabberID)) return generalReportTyping();
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        boolean bool = prefs.getBoolean(JabberID + "_reporttyping", true);
        return bool;
    }



    public static void setCustomPrivacyForSpecificContact(String JabberID, boolean affected) {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(JabberID, affected);
        edit.apply();
    }

    public static void setReportReadForSpecificContact(String JabberID, boolean affected) {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(JabberID + "_reportread", affected);
        edit.apply();
    }

    public static void setReportReceivedForSpecificContact(String JabberID, boolean affected) {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(JabberID + "_reportreceived", affected);
        edit.apply();
    }

    public static void setReportTypingForSpecificContact(String JabberID, boolean affected) {
        SharedPreferences prefs = Utils.context.getSharedPreferences("wamod_privacy", 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(JabberID + "_reporttyping", affected);
        edit.apply();
    }

    public static void initPrivacyOnChatInfo(final AppCompatActivity activity) {

        // TODO Fix this mess please

        com.whatsapp.qj contact = null;
        if (activity instanceof GroupChatInfo) contact = ((GroupChatInfo) activity).getContact((GroupChatInfo) activity);
        else if (activity instanceof ContactInfo) contact = ((ContactInfo) activity).f((ContactInfo) activity);
        if (contact == null) return;

        final String JabberID = contact.r;


        final ViewGroup wamod_privacy_card_customprivacy = (ViewGroup) activity.findViewById(Resources.getID("wamod_privacy_card_customprivacy"));
        /*final TextView wamod_privacy_card_customprivacy_title = (TextView) activity.findViewById(Resources.getID("wamod_privacy_card_customprivacy_title"));
        final TextView wamod_privacy_card_customprivacy_summary = (TextView) activity.findViewById(Resources.getID("wamod_privacy_card_customprivacy_summary"));*/
        final SwitchCompat wamod_privacy_card_customprivacy_switch = (SwitchCompat) activity.findViewById(Resources.getID("wamod_privacy_card_customprivacy_switch"));

        wamod_privacy_card_customprivacy_switch.setChecked(customPrivacyForSpecificContact(JabberID));
        wamod_privacy_card_customprivacy_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Privacy.setCustomPrivacyForSpecificContact(JabberID, b);
            }
        });

        wamod_privacy_card_customprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean reportreceived_original = reportReceivedForSpecificContact(JabberID);
                final boolean reportread_original     = reportReadForSpecificContact(JabberID);
                final boolean reporttyping_original   = reportTypingForSpecificContact(JabberID);
                final boolean customprivacy_original  = customPrivacyForSpecificContact(JabberID);

                wamod_privacy_card_customprivacy_switch.setChecked(true);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                View view1 = LayoutInflater.from(activity).inflate(activity.getResources().getLayout(Resources.getLayout("wamod_privacy_individual_dialog")), null);

                View wamod_privacy_card_reportreceived = view1.findViewById(Resources.getID("wamod_privacy_card_reportreceived"));
                View wamod_privacy_card_reportread = view1.findViewById(Resources.getID("wamod_privacy_card_reportread"));
                View wamod_privacy_card_reporttyping = view1.findViewById(Resources.getID("wamod_privacy_card_hidetyping"));
                final SwitchCompat wamod_privacy_card_reportreceived_switch = (SwitchCompat) view1.findViewById(Resources.getID("wamod_privacy_card_reportreceived_switch"));
                final SwitchCompat wamod_privacy_card_reportread_switch = (SwitchCompat) view1.findViewById(Resources.getID("wamod_privacy_card_reportread_switch"));
                final SwitchCompat wamod_privacy_card_reporttyping_switch = (SwitchCompat) view1.findViewById(Resources.getID("wamod_privacy_card_hidetyping_switch"));

                if (Utils.nightModeShouldRun()) {
                    View wamod_privacy_card = view1.findViewById(Resources.getID("wamod_privacy"));
                    TextView wamod_privacy_card_reportreceived_title = (TextView) view1.findViewById(Resources.getID("wamod_privacy_card_reportreceived_title"));
                    TextView wamod_privacy_card_reportreceived_summary = (TextView) view1.findViewById(Resources.getID("wamod_privacy_card_reportreceived_summary"));
                    TextView wamod_privacy_card_reportread_title = (TextView) view1.findViewById(Resources.getID("wamod_privacy_card_reportread_title"));
                    TextView wamod_privacy_card_reportread_summary = (TextView) view1.findViewById(Resources.getID("wamod_privacy_card_reportread_summary"));
                    TextView wamod_privacy_card_hidetyping_title = (TextView) view1.findViewById(Resources.getID("wamod_privacy_card_hidetyping_title"));
                    TextView wamod_privacy_card_hidetyping_summary = (TextView) view1.findViewById(Resources.getID("wamod_privacy_card_hidetyping_summary"));

                    wamod_privacy_card.setBackgroundColor(Utils.getDarkColor(2));
                    wamod_privacy_card_reportreceived_title.setTextColor(Utils.getDarkColor(0));
                    wamod_privacy_card_reportreceived_summary.setTextColor(Utils.getDarkColor(1));
                    wamod_privacy_card_reportread_title.setTextColor(Utils.getDarkColor(0));
                    wamod_privacy_card_reportread_summary.setTextColor(Utils.getDarkColor(1));
                    wamod_privacy_card_hidetyping_title.setTextColor(Utils.getDarkColor(0));
                    wamod_privacy_card_hidetyping_summary.setTextColor(Utils.getDarkColor(1));
                }


                wamod_privacy_card_reportreceived_switch.setChecked(reportreceived_original);
                wamod_privacy_card_reportread_switch.setChecked(reportread_original);
                wamod_privacy_card_reporttyping_switch.setChecked(reporttyping_original);


                wamod_privacy_card_reportreceived.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wamod_privacy_card_reportreceived_switch.setChecked(!wamod_privacy_card_reportreceived_switch.isChecked());
                    }
                });

                wamod_privacy_card_reportread.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wamod_privacy_card_reportread_switch.setChecked(!wamod_privacy_card_reportread_switch.isChecked());
                    }
                });

                wamod_privacy_card_reporttyping.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wamod_privacy_card_reporttyping_switch.setChecked(!wamod_privacy_card_reporttyping_switch.isChecked());
                    }
                });


                wamod_privacy_card_reportreceived_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        setReportReceivedForSpecificContact(JabberID, b);
                    }
                });

                wamod_privacy_card_reportread_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        setReportReadForSpecificContact(JabberID, b);
                    }
                });

                wamod_privacy_card_reporttyping_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        setReportTypingForSpecificContact(JabberID, b);
                    }
                });

                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        boolean reportreceivedChanged = reportreceived_original != reportReceivedForSpecificContact(JabberID);
                        boolean reportReadChanged = reportread_original != reportReadForSpecificContact(JabberID);
                        boolean reportTypingChanged = reporttyping_original != reportTypingForSpecificContact(JabberID);
                        if (!reportreceivedChanged && !reportReadChanged && !reportTypingChanged && !customprivacy_original) {
                            wamod_privacy_card_customprivacy_switch.setChecked(false);
                        }
                    }
                });

                alertDialog.setView(view1);

                alertDialog.setTitle(activity.getResources().getString(Resources.getString("wamod_privacy_card_customprivacy_title")));
                alertDialog.setPositiveButton(activity.getResources().getString(android.R.string.ok), null);

                Dialog dialog = alertDialog.create();
                dialog.show();
                View content = dialog.findViewById(android.R.id.content);
                if (Utils.nightModeShouldRun()) content.setBackgroundColor(Utils.getDarkColor(2));
            }
        });


        /*SwitchCompat reportReadSwitch     = (SwitchCompat) activity.findViewById(Resources.getID("wamod_privacy_card_reportread_switch"));
        SwitchCompat reportReceivedSwitch = (SwitchCompat) activity.findViewById(Resources.getID("wamod_privacy_card_reportreceived_switch"));
        SwitchCompat hideTypingSwitch     = (SwitchCompat) activity.findViewById(Resources.getID("wamod_privacy_card_hidetyping_switch"));
        if (reportReadSwitch == null || reportReceivedSwitch == null || hideTypingSwitch == null) return;

        com.whatsapp.qj contact = null;
        if (activity instanceof GroupChatInfo) contact = ((GroupChatInfo) activity).getContact((GroupChatInfo) activity);
        else if (activity instanceof ContactInfo) contact = ((ContactInfo) activity).f((ContactInfo) activity);
        if (contact == null) return;

        final String JabberID = contact.r;

        if (Privacy.contactAffectedByBlueTickMod(JabberID))
            reportReadSwitch.setChecked(false);
        else
            reportReadSwitch.setChecked(true);

        if (Privacy.contactAffectedBySecondTickMod(JabberID))
            reportReceivedSwitch.setChecked(false);
        else
            reportReceivedSwitch.setChecked(true);

        if (Privacy.contactAffectedByHideTypingMod(JabberID))
            hideTypingSwitch.setChecked(false);
        else
            hideTypingSwitch.setChecked(true);

        reportReadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Privacy.setContactAffectedByBlueTickMod(JabberID, !b);
            }
        });

        reportReceivedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Privacy.setContactAffectedBySecondTickMod(JabberID, !b);
            }
        });

        hideTypingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Privacy.setContactAffectedByHideTypingMod(JabberID, !b);
            }
        });*/
    }


    /* Trash */

    public static void logStringArray(String[] strings) {
        if (strings == null) {
            Log.i("WAMOD_PRIVACY", "NULL STRING LOL");
            return;
        }

        String str = "";
        for (String s : strings) {
            str += s + " ---- ";
        }
        Log.i("WAMOD_PRIVACY", str);
    }

    public static void logP(g g) {
        Log.i("WAMOD_PRIVACY", "a: " + g.a + " -- b: " + (g.b? "true" : "false") + " -- c: " + g.c);
    }

    public static void log(g g, String str1, String str2, String[] str3, String str4) {
        int i = 1;
        Log.i("WAMOD_PRIVACY_READ", "a: " + g.a + " -- b: " + (g.b? "true" : "false") + " -- c: " + g.c);
        Log.i("WAMOD_PRIVACY_READ", "1: " + str1 + " -- 2: " + str2 + " -- 4: " + str4);
        if (str3 != null)
            for (String s : str3) {
                Log.i("WAMOD_PRIVACY_READ", i + ": " + s);
                i++;
            }
        else Log.i("WAMOD_PRIVACY_READ", "String 3: NULL");
    }

    public static void logReceived(g g, String str1, String str2, String[] str3, String str4) {
        int i = 1;
        Log.i("WAMOD_PRIVACY_RECEIVED", "a: " + g.a + " -- b: " + (g.b? "true" : "false") + " -- c: " + g.c);
        Log.i("WAMOD_PRIVACY_RECEIVED", "1: " + str1 + " -- 2: " + str2 + " -- 4: " + str4);
        if (str3 != null)
            for (String s : str3) {
                Log.i("WAMOD_PRIVACY_RECEIVED", i + ": " + s);
                i++;
            }
    }

    public static void logStrings(String[] strings) {
        if (stringsDecoded) return;
        int i = 1;
        for (String s : strings) {
            Log.i("WAMOD_PRIVACY_STRINGS", i + ": " + s);
            i++;
        }
        stringsDecoded = true;
    }
}
