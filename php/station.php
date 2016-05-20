<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);
$command = $_GET['cmd'];

	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450atm10';
	$username = '_450atm10';
	$password = 'afimmit';

	try {
		$db = new PDO($dsn, $username, $password);
		
		$email = isset($_GET['email']) ? $_GET['email'] : '';
		//build select query to obtain that user's station data
        $sql = "SELECT userID FROM users ";
        $sql .= " WHERE email = '$email'";
		//$userID_query = $db->query($sql);
		//$userID_query->execute();
		//$result = $userID_query->fetchAll(PDO::FETCH_ASSOC); 
		//$userID = $result;
		
		$userID_query = $db->prepare($sql);
		$userID_query->execute();
		$result = $userID_query->fetchAll(); 
		$userID = $result;
		
		//$stmt = $db->prepare($sql); 
		//$stmt->execute();
		// set the resulting array to associative
		//$result = $stmt->setFetchMode(PDO::FETCH_ASSOC);
		//$userID = $result['userID'];
		
		if ($command == "station") {
			$select_sql = "SELECT lists.title, lists.isDeleted FROM lists JOIN listRights "
			$select_sql .= "ON lists.noteID = listRights.listID WHERE listRights.userID = '$userID'";
			$list_query = $db->query($select_sql);
			$lists = $list_query->fetchAll(PDO::FETCH_ASSOC);
			if ($lists) {	
				echo json_encode($lists);
			}
		}
		else if ($command == "instructors") {
			$select_sql = 'SELECT fullName, title, email, photoUrl, office FROM Instructors';
			$query = $db->query($select_sql);
			$instructors = $query->fetchAll(PDO::FETCH_ASSOC);
			if ($instructors) {
				echo json_encode($instructors);
			}
		}
		$db = null;
	} catch (PDOException $e) {
		$error_message = $e->getMessage();
		echo 'There was an error connecting to the database.';
		echo $error_message;
		exit();
	}

?>
