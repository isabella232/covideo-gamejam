extends Node

# original source https://docs.godotengine.org/en/3.2/tutorials/networking/websocket.html

# The URL we will connect to
export var websocket_url = "ws://echo.websocket.org"

# Our WebSocketClient instance
var _client = WebSocketClient.new()

func just_connect():
	var err = _client.connect_to_url(websocket_url)
	if err != OK:
		print("Unable to connect")
		set_process(false)

func _ready():
	_client.connect("connection_closed", self, "_closed")
	_client.connect("connection_error", self, "_closed")
	_client.connect("connection_established", self, "_connected")
	_client.connect("data_received", self, "_on_data")

func _closed(was_clean = false):
	# was_clean will tell you if the disconnection was correctly notified
	# by the remote peer before closing the socket.
	print("Closed, clean: ", was_clean)
	set_process(false)

func _connected(proto = ""):
	# This is called on connection, "proto" will be the selected WebSocket
	# sub-protocol (which is optional)
	print("Connected with protocol: ", proto)
	# You MUST always use get_peer(1).put_packet to send data to server,
	# and not put_packet directly when not using the MultiplayerAPI.
	var json = JSON.print({ "method": "echo", "data": "deadbeef" })
	_client.get_peer(1).put_packet(json.to_utf8())

func _on_data():
	var json = _client.get_peer(1).get_packet().get_string_from_utf8()

	var result = JSON.parse(json)
	if result.error:
		printerr(result.error_string)
	else:
		print("Got data from server: ", result.result)

func _process(_delta):
	_client.poll()
	
