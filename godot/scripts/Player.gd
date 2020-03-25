extends Character
class_name Player

func _process(delta):
	var velocity = Vector2() 
	if Input.is_action_pressed("ui_right"):
		velocity.x += 1
		_walk_animation_right()
	if Input.is_action_pressed("ui_left"):
		velocity.x -= 1
		_walk_animation_left()
	if Input.is_action_pressed("ui_down"):
		velocity.y += 1
		_walk_animation_down()
	if Input.is_action_pressed("ui_up"):
		velocity.y -= 1
		_walk_animation_up()
		
	if velocity.length() > 0:
		velocity = velocity.normalized() * speed
	else:
		$AnimatedSprite.play("front")
		
	move_and_slide(velocity)

func _on_collision(colliding_body):
	#print("player collision " + colliding_body.name)
	pass
