extends Character
class_name NPC


var npc_think_time = 0.4
var npc_think_timer = null
var timer_running = false

var random_movement_time = 2
var random_movement_timer = null
var random_movement_direction : Vector2 = Vector2.ZERO

export var npc_type = "retired_person"

var targets = []
var current_target : RigidBody2D = null

var rng = RandomNumberGenerator.new()

func _ready():
	_setup_npc_think_time_timer()
	_setup_random_movement_timer()
	_setup_npc_type()
	add_to_group("npcs")


func _on_collision(colliding_body):
	if ((colliding_body is RigidBody2D)):
		if (colliding_body.get_groups().has("tprs")):
			var index = targets.find(colliding_body)
			if(index != -1):
				targets.remove(index)
				_find_closest_target()


func _setup_npc_type():
	match npc_type:
		"retired_person":
			speed = 10
			strength = 5
			
		"yolo_hipster":
			speed = 20
			strength = 10
		"family_mum":
			speed = 30
			strength = 30
			npc_think_time = 0.25


func _add_target(newtarget):
	targets.append(newtarget)
	if (current_target == null && !timer_running):
		_find_closest_target()


func _find_closest_target():
	if targets.size() == 0:
		current_target = null
		pass
	else:	
		var closest_distance = targets[0].global_position.distance_to(self.global_position)
		var closest_target = targets[0]
		for target in targets:
			var distance = target.global_position.distance_to(self.global_position)
			if (distance < closest_distance):
				closest_target = target
				closest_distance = distance
		
		current_target = closest_target


func _move_to_target():
	var target_direction = _get_direction_to_target()
	var movement_direction = target_direction.normalized() * speed
	move_and_slide(movement_direction)


func _get_direction_to_target():
	if current_target != null:
		return current_target.global_position - global_position
	else:
		return Vector2.ZERO


# move into a random direction; if new_rand_direction is true, a new randomized direction is used
func _move_randomly(new_rand_direction):
	if (new_rand_direction):
		random_movement_direction = _get_random_direction()
	move_and_slide(random_movement_direction.normalized() * speed)


func _get_random_direction():
	rng.randomize()
	var x = rng.randf_range(-1, 1)
	var y = rng.randf_range(-1, 1)
	return  Vector2(x, y)


func _setup_npc_think_time_timer():
	npc_think_timer = Timer.new()
	npc_think_timer.set_one_shot(true)
	npc_think_timer.set_wait_time(npc_think_time)
	npc_think_timer.connect("timeout", self, "_on_think_timer_timeout")
	add_child(npc_think_timer)


func _on_think_timer_timeout():
	pass


func _setup_random_movement_timer():
	random_movement_timer = Timer.new()
	random_movement_timer.set_one_shot(true)
	random_movement_timer.set_wait_time(random_movement_time)
	random_movement_timer.connect("timeout", self, "_on_random_movement_timer_timeout")
	add_child(random_movement_timer)


func _on_random_movement_timer_timeout():
	_find_closest_target()
	timer_running = false


func _physics_process(delta):
	# if npc is not moving around randomly and a target has been set, try to move to it
	if (!timer_running && current_target != null):
		var space_state = $CollisionShape2D.get_world_2d().direct_space_state
		var result_movement_dir = space_state.intersect_ray(global_position, current_target.global_position, [self])
		
		# check if path to tpr is obstructed; if not, move to the tpr, else move in a rnd direction
		if (result_movement_dir):
			if (result_movement_dir["collider"].get_groups().has("tprs")):
				_move_to_target()
			else:
				_start_random_movement_and_timer()
				
	# if the we have not yet started to move in a random direction, start to do that now
	elif (!timer_running && current_target == null):
		_start_random_movement_and_timer()
	
	# npc is moving in a random direction for the duration of the timer. continue to do this
	elif (timer_running):
		_move_randomly(false)


func _start_random_movement_and_timer():
	timer_running = true
	current_target = null 
	_move_randomly(true)
	random_movement_timer.start()
