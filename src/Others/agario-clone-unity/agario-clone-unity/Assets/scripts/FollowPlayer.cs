using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FollowPlayer : MonoBehaviour {
	GameObject player;

	void Awake() {
		player = GameObject.Find("player");
	}
	
	void LateUpdate() {
		followPlayer ();
	}

	/**
	 * Follow player on x and y axis
	 */
	void followPlayer() {
		Vector3 playerPosition = player.transform.position;
		transform.position = new Vector3(playerPosition.x, playerPosition.y, transform.position.z);
	}
}