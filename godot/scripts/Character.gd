# base class for characters
extends Node

var position

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
