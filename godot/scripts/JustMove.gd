extends Node2D

export var speed = 200.0

func _process(delta):
	var input = Vector2(0, 0)
	if Input.is_key_pressed(KEY_W):
		input.y -= 1
	if Input.is_key_pressed(KEY_S):
		input.y += 1
	if Input.is_key_pressed(KEY_A):
		input.x -= 1
	if Input.is_key_pressed(KEY_D):
		input.x += 1
	translate(input.normalized() * speed * delta)
