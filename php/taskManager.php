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
		if($command == "getAll"){
			$listID = $_GET['id'];
			$textInput = '';
			
			$select_sql = "SELECT tasks.taskID, tasks.textInput, tasks.isDeleted FROM tasks JOIN taskRights ON tasks.taskID = taskRights.taskID WHERE taskRights.listID=$listID";

			$query = $db->query($select_sql);
			$tasks = $query->fetchAll(PDO::FETCH_ASSOC);
			if ($tasks) {	
				echo json_encode($tasks);
			} else {
				echo json_encode('"result":"Failure');
			}
			
		} else if($command == "add"){
			$textInput = $_GET['textInput'];
			$textInput = urldecode($textInput);
			
			$sql = "INSERT INTO tasks";
			$sql .= " VALUES (DEFAULT, '$textInput', 0)";

			if ($db->query($sql)) {
				echo '{"result":"successfully created a record for table: tasks "}';

				//obtains taskID that was just stored
				$taskID = $db->lastInsertId();
				$listID = $_GET['list'];

				$sql = "INSERT INTO taskRights";
				$sql .= " VALUES ('O', '$taskID', '$listID')";

				//attempts to add listRights
				if ($db->query($sql)) {
					echo '{"result": "successfully created a record for table: taskRights"}';
				} 
			}				
		} else if ($command == "edit") {
			$taskID = $_GET['id'];
			$newText = urldecode($_GET['newtext']);
			$sql = "UPDATE tasks SET textInput = '$newText' WHERE taskID = $taskID;";
			if ($db->query($sql)) {
				echo '{"result": "successfully updated record"}';
			}
		} else if ($command == "delete"){
			$taskID = $_GET['id'];
			$sql = "DELETE FROM _450atm10.tasks WHERE taskID = $taskID";
			if ($db->query($sql)) {
				echo 'you are here';
				$sql = "DELETE FROM _450atm10.taskRights WHERE taskID = $taskID";
				if ($db->query($sql)) {
					echo '{"result": "successfully removed record"}';
				} 
			} 
		} else {
			echo "Something's wrong with the G-Diffuser!";
		}
		
		$db = null;			
	} catch(PDOException $e) {
		if ((int)($e->getCode()) == 23000) {
			echo '{"result": "fail", "error": "That task already exists."}';
			} else {
			echo 'Error Number: ' . $e->getCode() . '<br>';
			echo '{"result": "
			", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
		}
	}
} else {
	echo "Your URL is bad!";
}
?>
