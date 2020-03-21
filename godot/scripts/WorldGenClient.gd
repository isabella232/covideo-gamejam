extends HTTPRequest

export var world_gen_url = "http://localhost:8080"
export var create_map_path = "/map/create"

func create_map(config):
	return _json_request(world_gen_url + create_map_path)
	
func _json_request(req_url):
	var err = request(req_url)
	if err != OK: return printerr("request error {}".format(err))
	var res = yield(self, "request_completed")
	var body = res[3].get_string_from_utf8()
	var json = JSON.parse(body)
	if json.error: return printerr("response error {0} {1} {2}, got\n{3}".format([json.error, json.error_string, json.error_line, body]))
	return json.result
