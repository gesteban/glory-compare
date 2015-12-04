package deprecated;

public enum GroupID {

	Assault_Ship(100), Assembly_Array(10), Attack_Battlecruiser(300), Battlecruiser(
			250), Battleship(750), Black_Ops(1800), Blockade_Runner(125), Capital_Industrial_Ship(
			1000), Capsule(5), Carrier(3000), Combat_Recon_Ship(350), Command_Ship(
			450), Control_Tower(250), Corporate_Hangar_Array(50), Covert_Ops(80), Cruiser(
			100), Cynosural_Generator_Array(10), Cynosural_System_Jammer(50), Destroyer(
			60), Dreadnought(4000), Electronic_Attack_Ship(200), Electronic_Warfare_Battery(
			50), Energy_Neutralizing_Battery(50), Exhumer(20), Force_Recon_Ship(
			350), Freighter(300), Frigate(50), Heavy_Assault_Ship(400), Heavy_Interdictor(
			600), Industrial(20), Industrial_Command_Ship(800), Infrastructure_Hubs(
			500), Interceptor(60), Interdictor(60), Jump_Freighter(500), Jump_Portal_Array(
			10), Logistics(175), Marauder(1000), Mining_Barge(20), Mobile_Hybrid_Sentry(
			10), Mobile_Laboratory(10), Mobile_Laser_Sentry(10), Mobile_Missile_Sentry(
			10), Mobile_Projectile_Sentry(10), Mobile_Reactor(10), Moon_Mining(
			10), Orbital_Construction_Platform(5), Orbital_Infrastructure(500), Prototype_Exploration_Ship(
			5), Refining_Array(10), Rookie_ship(5), Scanner_Array(10), Sensor_Dampening_Battery(
			10), Shield_Hardening_Array(10), Ship_Maintenance_Array(10), Shuttle(
			5), Silo(10), Sovereignty_Blockade_Units(250), Stasis_Webification_Battery(
			10), Stealth_Bomber(80), Strategic_Cruiser(750), Supercarrier(6000), Tactical_Destroyer(
			250), Territorial_Claim_Units(500), Titan(20000), Tracking_Array(10), Transport_Ship(
			30), Warp_Scrambling_Battery(10);

	public final int points;

	GroupID(int points) {
		this.points = points;
	}

}
