package org.retroshare.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rsctrl.core.Core;
import rsctrl.core.Core.Person;
import rsctrl.msgs.Msgs;
import rsctrl.peers.Peers;


public class PeerDetailsActivity extends ProxiedActivityBase
{
	public String TAG() { return "PeerDetailsActivity"; }

	public final static String PGP_ID_EXTRA = "pgpId";
	private String pgpId;

    @Override
    public void onCreateBeforeConnectionInit(Bundle savedInstanceState)
	{
		pgpId = getIntent().getStringExtra(PGP_ID_EXTRA);

		setContentView(R.layout.activity_peerdetails);
    }

	@Override
	public void onServiceConnected()
	{
		Person p = getConnectedServer().mRsPeersService.getPersonByPgpId(pgpId);

		TextView nameTextView = (TextView) findViewById(R.id.peerNameTextView);
		TextView pgpIdTextView = (TextView) findViewById(R.id.pgpIdTextView);
		Button toggleFriendshipButton = (Button) findViewById(R.id.buttonToggleFriendship);

		nameTextView.setText(p.getName());
		pgpIdTextView.setText(pgpId);

		Person.Relationship r = p.getRelation();
		if ( r.equals(Person.Relationship.YOURSELF) ) toggleFriendshipButton.setVisibility(View.GONE);
		else if ( r.equals(Person.Relationship.FRIEND) ) toggleFriendshipButton.setText(R.string.block_friend);
		else toggleFriendshipButton.setText(R.string.add_as_friend);
	}

	public void onToggleFriendshipButtonPressed(View v)
	{
		if(isBound())
		{
			RsPeersService prs = getConnectedServer().mRsPeersService;
			Person p = prs.getPersonByPgpId(pgpId);

			prs.requestToggleFriendShip(p);
			prs.requestPersonsUpdate(Peers.RequestPeers.SetOption.ALL, Peers.RequestPeers.InfoOption.ALLINFO);

			finish();
		}
	}
}
