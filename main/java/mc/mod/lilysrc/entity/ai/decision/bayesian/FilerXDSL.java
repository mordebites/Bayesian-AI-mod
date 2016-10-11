package mc.mod.lilysrc.entity.ai.decision.bayesian;


/**
 * Long string which contains the converted network file (.xdsl)
 */
public class FilerXDSL {

	static final String NET = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
"<smile version=\"1.0\" id=\"Network1\" numsamples=\"1000\" discsamples=\"10000\">" +
"	<nodes>" +
"		<cpt id=\"State_t\">" +
"			<state id=\"Hunt\" />" +
"			<state id=\"Suspect\" />" +
"			<state id=\"Flee\" />" +
"			<state id=\"LookAround\" />" +
"			<state id=\"Trick\" />" +
"			<probabilities>0.2 0.2 0.2 0.2 0.2</probabilities>" +
"		</cpt>" +
"		<cpt id=\"State_t1\">" +
"			<state id=\"Hunt\" />" +
"			<state id=\"Suspect\" />" +
"			<state id=\"Flee\" />" +
"			<state id=\"LookAround\" />" +
"			<state id=\"Trick\" />" +
"			<parents>State_t</parents>" +
"			<probabilities>0.29999 0.35 0.1 0.25 1e-05 0.33326666 0.33326666 0.0001 0.33326666 0.0001 0.1 0.1 0.3 0.4 0.1 0.2 0.2 0.2 0.2 0.2 0.1699071303071843 0.1699071303071843 0.1784604097078756 0.2408626648388778 0.2408626648388779</probabilities>" +
"		</cpt>" +
"		<cpt id=\"Player_In_Sight\">" +
"			<state id=\"Close\" />" +
"			<state id=\"Far\" />" +
"			<state id=\"None\" />" +
"			<parents>State_t1</parents>" +
"			<probabilities>0.5 0.5 0 0 0 1 0.6 0.39992 8.000000000000001e-05 0 0 1 0 0 1</probabilities>" +
"		</cpt>" +
"		<cpt id=\"Player_Tricking\">" +
"			<state id=\"Unlikely\" />" +
"			<state id=\"Uncertain\" />" +
"			<state id=\"Likely\" />" +
"			<parents>State_t1</parents>" +
"			<probabilities>0.3333333333333334 0.3333333333333333 0.3333333333333334 0.98 0.01 0.01 0.45 0.1 0.45 0.25 0.38 0.37 0.25 0.38 0.37</probabilities>" +
"		</cpt>" +
"		<cpt id=\"Step_Sound\">" +
"			<state id=\"Close\" />" +
"			<state id=\"Far\" />" +
"			<state id=\"None\" />" +
"			<parents>Player_Tricking</parents>" +
"			<probabilities>0.95 0.04999000000000004 1e-05 1e-07 0.9999998000000001 1e-07 0.15 0.15 0.7</probabilities>" +
"		</cpt>" +
"		<cpt id=\"Timer\">" +
"			<state id=\"RunningOut\" />" +
"			<state id=\"Normal\" />" +
"			<parents>State_t1</parents>" +
"			<probabilities>1e-06 0.999999 1e-06 0.999999 0.9999999000000001 1e-07 0.0001 0.9999 0.0001 0.9999</probabilities>" +
"		</cpt>" +
"		<cpt id=\"Lighting_Change\">" +
"			<state id=\"Close\" />" +
"			<state id=\"Far\" />" +
"			<state id=\"None\" />" +
"			<parents>Player_Tricking</parents>" +
"			<probabilities>0.799 0.2 0.001 0.001 0.5 0.499 0.799 0.2 0.001</probabilities>" +
"		</cpt>" +
"		<cpt id=\"Sound_Block\">" +
"			<state id=\"Close\" />" +
"			<state id=\"Far\" />" +
"			<state id=\"None\" />" +
"			<parents>Player_Tricking</parents>" +
"			<probabilities>0.7999900000000001 0.2 1e-05 1e-05 0.5 0.49999 0.7999900000000001 0.2 1e-05</probabilities>" +
"		</cpt>" +
"	</nodes>" +
"	<extensions>" +
"		<genie version=\"1.0\" app=\"GeNIe 2.1.380.0\" name=\"Network1\" faultnameformat=\"nodestate\">" +
"			<node id=\"State_t1\">" +
"				<name>State t+1</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>587 58 635 88</position>" +
"				<barchart active=\"true\" width=\"174\" height=\"108\" />" +
"			</node>" +
"			<node id=\"Player_In_Sight\">" +
"				<name>Player In Sight</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>167 323 238 367</position>" +
"				<barchart active=\"true\" width=\"120\" height=\"72\" />" +
"			</node>" +
"			<node id=\"Step_Sound\">" +
"				<name>Step Sound</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>407 309 476 352</position>" +
"				<barchart active=\"true\" width=\"128\" height=\"72\" />" +
"				<defcomment row=\"0\" col=\"0\">We want to determine how much the next state depends on hearing a sound at a certain distance\\n</defcomment>" +
"			</node>" +
"			<node id=\"Timer\">" +
"				<name>Timer</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>178 217 226 247</position>" +
"				<barchart active=\"true\" width=\"118\" height=\"54\" />" +
"			</node>" +
"			<node id=\"Lighting_Change\">" +
"				<name>Lighting Change</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>565 323 653 378</position>" +
"				<barchart active=\"true\" width=\"139\" height=\"72\" />" +
"			</node>" +
"			<node id=\"Sound_Block\">" +
"				<name>Sound Block</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>755 320 828 365</position>" +
"				<barchart active=\"true\" width=\"128\" height=\"72\" />" +
"			</node>" +
"			<node id=\"Player_Tricking\">" +
"				<name>Player Tricking</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>567 178 649 229</position>" +
"				<barchart active=\"true\" width=\"128\" height=\"72\" />" +
"				<defcomment row=\"2\" col=\"1\">If it&apos;s likely or unlikely, it considers the player is not in sight, probably will add a rule to that in the code</defcomment>" +
"			</node>" +
"			<node id=\"State_t\">" +
"				<name>State t</name>" +
"				<interior color=\"e5f6f7\" />" +
"				<outline color=\"000080\" />" +
"				<font color=\"000000\" name=\"Arial\" size=\"8\" />" +
"				<position>182 109 230 139</position>" +
"				<barchart active=\"true\" width=\"159\" height=\"108\" />" +
"			</node>" +
"		</genie>" +
"	</extensions>" +
"</smile>";		
}
