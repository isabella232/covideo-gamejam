extends KinematicBody2D
class_name Character

var speed = 70
var strength = 1
var inventory = []

func _ready():
	var collision_listener = $Area2D.connect("body_entered", self, "_on_collision")

func _process(delta):
	pass

# called when the character collides with something
func _on_collision(colliding_body):
	print("collision " + colliding_body.name)

# set a different sprite
func _change_sprite():
	pass

func _walk_animation_up():
	$AnimatedSprite.play("back")
	

func _walk_animation_right():
	$AnimatedSprite.play("right")
	$AnimatedSprite.flip_h = false
	
	
func _walk_animation_down():
	$AnimatedSprite.play("front")


func _walk_animation_left():
	$AnimatedSprite.play("right")
	$AnimatedSprite.flip_h = true
