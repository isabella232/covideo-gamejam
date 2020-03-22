extends Character

func _process(delta):
	var velocity = Vector2() 
	if Input.is_action_pressed("ui_right"):
		velocity.x += 1
		_walk_animation_right()
	elif Input.is_action_pressed("ui_left"):
		velocity.x -= 1
		_walk_animation_left()
	elif Input.is_action_pressed("ui_down"):
		velocity.y += 1
		_walk_animation_down()
	elif Input.is_action_pressed("ui_up"):
		velocity.y -= 1
		_walk_animation_up()
		
	if velocity.length() > 0:
		velocity = velocity.normalized() * speed
	else:
		$KinematicBody2D/AnimatedSprite.play("front")
		
	$KinematicBody2D.move_and_slide(velocity)

func _on_collision(colliding_body):
	print("player collision " + colliding_body.name)
