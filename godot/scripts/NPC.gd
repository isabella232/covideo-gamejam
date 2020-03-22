extends Character

var tile_size = 5

var npc_think_time = 0.4
var npc_think_timer = null

export var npc_type = "retired_person"

var target : Node = null

var result_up
var result_right
var result_down
var result_left

func _ready():
	_setup_npc_think_time_timer()
	_setup_npc_type()
	_find_target()


#func _process(delta):
#	pass


func _on_collision(colliding_body):
	print("npc collision " + colliding_body.name)

func _setup_npc_type():
	match npc_type:
		"retired_person":
			speed = 90
			strength = 5
		"yolo_hipster":
			speed = 120
			strength = 10
		"family_mum":
			speed = 150
			strength = 30
			npc_think_time = 0.25


func _find_target():
	var scene_parent = self.find_parent("DEV")
	if scene_parent == null:
		print("could not find scene")
	else:
		var target_object = scene_parent.find_node("Player")
		if target_object == null:
			print("could not find target")
		else:
			print(target_object.name)
			target = target_object.get_child(0)


func _move():
	var target_direction = _get_direction_to_target()
	var direction_to_move = _calculate_route(target_direction)
	var movement_direction = direction_to_move.normalized() * tile_size * speed
	print(movement_direction)
	$KinematicBody2D.move_and_slide(movement_direction)
	
	
func _get_direction_to_target():
	if target != null:
		return target.position - self.position
	else:
		print("target is null!")
		return Vector2(1,1)

func _setup_npc_think_time_timer():
	npc_think_timer = Timer.new()
	npc_think_timer.set_one_shot(true)
	npc_think_timer.set_wait_time(npc_think_time)
	npc_think_timer.connect("timeout", self, "_on_think_timer_timeout")
	add_child(npc_think_timer)
	npc_think_timer.start()
	print("npc timer set up")

func _on_think_timer_timeout():
	#print("npc timeout")
	if target != null:
		_move()
		npc_think_timer.start()
	else:
		print("target is null")
	
func _physics_process(delta):
	var space_state = $KinematicBody2D/CollisionShape2D.get_world_2d().direct_space_state
	result_up = space_state.intersect_ray(self.position, Vector2(position.x, position.y + 50))
	result_right = space_state.intersect_ray(self.position, Vector2(position.x + 50, position.y))
	result_down = space_state.intersect_ray(self.position, Vector2(position.x, position.y - 50))
	result_left = space_state.intersect_ray(self.position, Vector2(position.x - 50, position.y))
	

func _calculate_route(targetdirection):
	if (result_up == null || result_down == null || result_right == null ||result_left == null):
		return Vector2(0, 0)
	return targetdirection
	
	
	
