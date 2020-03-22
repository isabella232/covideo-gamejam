extends Node
class_name Character


var position = Vector2(0, 0)
var speed = 70
var strength = 1
var inventory = []

func _ready():
	var collision_listener = $KinematicBody2D/Area2D.connect("body_entered", self, "_on_collision")


func _process(delta):
	position = self.position
	
	
func _get_position():
	return position


# called when the character collides with something
func _on_collision(colliding_body):
	print("collision " + colliding_body.name)

# set a different sprite
func _change_sprite():
	pass

func _walk_animation_up():
	$KinematicBody2D/AnimatedSprite.play("back")
	

func _walk_animation_right():
	$KinematicBody2D/AnimatedSprite.play("right")
	$KinematicBody2D/AnimatedSprite.flip_h = false
	
	
func _walk_animation_down():
	$KinematicBody2D/AnimatedSprite.play("front")


func _walk_animation_left():
	$KinematicBody2D/AnimatedSprite.play("right")
	$KinematicBody2D/AnimatedSprite.flip_h = true
