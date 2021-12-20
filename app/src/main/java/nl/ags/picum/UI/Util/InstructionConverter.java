package nl.ags.picum.UI.Util;

import android.content.Context;

import androidx.annotation.Nullable;

import nl.ags.picum.R;

public class InstructionConverter {
    private static String[] instructions = {"@string/turn_left",
                                            "@string/turn_right",
                                            "@string/sharp_left",
                                            "@string/sharp_right",
                                            "@string/slight_left",
                                            "@string/slight_right",
                                            "@string/go_straight",
                                            "@string/enter_roundabout",
                                            "@string/exit_roundabout",
                                            "@string/u_turn",
                                            "@string/goal",
                                            "@string/depart",
                                            "@string/keep_left",
                                            "@string/keep_right"};
    public static String getInstruction(Context context, int id, String name, @Nullable String instruction){
        String formattedInstruction = "";
        formattedInstruction += context.getResources().getString(context.getResources().getIdentifier(instructions[id], null, context.getPackageName()));
        if(id == 11){
            formattedInstruction += " " + context.getResources().getString(context.getResources().getIdentifier("@string/" + instruction.split(" ")[1], null, context.getPackageName()));
        }
        if(!name.equals("-")){
            formattedInstruction += " "+ context.getResources().getString(R.string.instruction_on) + " " + name;
        }
        return formattedInstruction;
    }
}
