<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);
$command = $_GET['cmd'];

if($command != null){
	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450atm10';
	$username = '_450atm10';
	$password = 'afimmit';

	try {
		$db = new PDO($dsn, $username, $password);
		$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		//$command = isset($_GET['cmd']) ? $_GET['cmd'] : '';
		if($command == "getAll"){
			$listID = $_GET['id'];	
			
			$select_sql = "SELECT tasks.taskID, tasks.textInput, tasks.isDeleted FROM tasks JOIN taskRights ON tasks.taskID = taskRights.taskID WHERE taskRights.listID=$listID";

			$query = $db->query($select_sql);
			$tasks = $query->fetchAll(PDO::FETCH_ASSOC);
			if ($tasks) {	
				echo json_encode($tasks);
			} else {
				echo "Something went horribly wrong";
			}
			
		} else if($command == "add"){
			$taskText = $_GET['textInput'];
			$sql = "INSERT INTO tasks";
			$sql .= " VALUES (DEFAULT, '$taskText', 0)";

			if ($db->query($sql)) {
				echo '{"result": "successfully created a record for table: tasks "}';

				//obtains taskID that was just stored
				$taskID = $db->lastInsertId();
				$whichList = $_GET['list'];

				$sql = "INSERT INTO taskRights";
				$sql .= " VALUES ('O', '$taskID', '$whichList')";

				//attempts to add listRights
				if ($db->query($sql)) {
					echo '{"result": "successfully created a record for table: taskRights"}';
				} 
			}				
		}
		$db = null;			
	} catch(PDOException $e) {
		if ((int)($e->getCode()) == 23000) {
			echo '{"result": "fail", "error": "That list already exists."}';
			} else {
			echo 'Error Number: ' . $e->getCode() . '<br>';
			echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
		}
	}
} else {
	echo "Your URL is bad!";
}
?>
