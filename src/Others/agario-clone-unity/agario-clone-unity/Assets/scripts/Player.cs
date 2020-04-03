using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Player : MonoBehaviour {

	public float startingSize;
	public float startingMovementDelay;
	public float speed;
	public float sizeAugmenter;
	Rigidbody2D rigidBody2D;
	FoodSpawn FoodSpawn;

	void Awake() {
		rigidBody2D = GetComponent<Rigidbody2D>();
		FoodSpawn = GameObject.Find("_GameManager").GetComponent<FoodSpawn>();		
		transform.localScale = new Vector3(startingSize, startingSize, 0);
		GetComponent<SpriteRenderer>().color = getRandomColor();
	}

	void FixedUpdate() {
		if (isMouseInsideViewport()) Move();
		else StopMoving();
	}

	void OnTriggerEnter2D(Collider2D other) {
		if (other.gameObject.tag == "food") {
			getBigger();
			FoodSpawn.availableFoodCount--;
			Destroy(other.gameObject);
		}
	}

	/* Custom Functions */
	void getBigger() {
		transform.localScale += new Vector3(sizeAugmenter, sizeAugmenter, 0);
	}

	void Move() {
		if (Time.time < startingMovementDelay) return;

		Vector2 targetPosition = Camera.main.ScreenToWorldPoint(Input.mousePosition);
		float movementSpeed = speed * Time.fixedDeltaTime;
		Vector2 movement = Vector2.MoveTowards(transform.position, targetPosition, movementSpeed);
		rigidBody2D.MovePosition(movement);
	}

	void StopMoving() {
		rigidBody2D.velocity = Vector2.zero;
	}

	bool isMouseInsideViewport() {
		Rect screen = new Rect(0, 0, Screen.width, Screen.height);
		return screen.Contains(Input.mousePosition);
	}

	Color getRandomColor() {
		return new Color(Random.value,Random.value, Random.value);
	}

}
