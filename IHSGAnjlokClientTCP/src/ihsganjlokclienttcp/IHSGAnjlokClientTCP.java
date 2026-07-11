/*
 * Decompiled with CFR 0.152.
 */
package ihsganjlokclienttcp;

import ihsganjlokclienttcp.FormLogin;
import java.awt.EventQueue;

public class IHSGAnjlokClientTCP {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                new FormLogin().setVisible(true);
            }
        });
    }
}

