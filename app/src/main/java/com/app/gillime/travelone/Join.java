package com.app.gillime.travelone;

/**
 * Created by Charles on 11/20/16.
 */

public class Join {
    private String inviteeName;

    public Join() {

    }

    public Join(String invitee) {
        this.inviteeName = invitee;
    }

    public String getInvitee() {
        return inviteeName;
    }

    public void setInvitee(String invitee) {
        this.inviteeName = invitee;
    }
}
