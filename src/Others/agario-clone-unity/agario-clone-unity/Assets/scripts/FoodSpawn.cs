using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FoodSpawn : MonoBehaviour {

	public GameObject food;
	public float spawnPeriod;
	public int startFoodAvailable;
	public int maximumFoodAvailable;
	public int availableFoodCount;
	
	GameObject spawningArea;
	float nextSpawnTime;

	void Awake() {
		nextSpawnTime = 0f;
		spawningArea = GameObject.Find("spawningArea");

		availableFoodCount = 0;
		for (int i=0; i<startFoodAvailable; i++) Spawn();
	}

	void Update() {
		if (Time.time > nextSpawnTime) {
			nextSpawnTime += spawnPeriod;
			Spawn();
		}
	}

	void Spawn () {
		Vector2 position = getRandomPosition();
		position = spawningArea.transform.TransformPoint(position * .5f);

		GameObject _food = Instantiate(food, position, Quaternion.identity);
		_food.transform.SetParent(spawningArea.transform);
 
		availableFoodCount++;
	}

	Vector2 getRandomPosition() {
		return new Vector2(Random.Range(-1f, 1f), Random.Range(-1f, 1f));
	}
}