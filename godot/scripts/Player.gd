extends "res://scripts/Character.gd"

func _process(delta):
	var velocity = Vector2()  # The player's movement vector.
	if Input.is_action_pressed("ui_right"):
		velocity.x += 1
	elif Input.is_action_pressed("ui_left"):
		velocity.x -= 1
	if Input.is_action_pressed("ui_down"):
		velocity.y += 1
	elif Input.is_action_pressed("ui_up"):
		velocity.y -= 1
	if velocity.length() > 0:
		velocity = velocity.normalized() * speed
		$KinematicBody2D/AnimatedSprite.play("right")
	else:
		$KinematicBody2D/AnimatedSprite.play("front")
		
	$KinematicBody2D.move_and_slide(velocity)
