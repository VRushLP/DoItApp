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
		
		$userID = isset($_GET['userID']) ? $_GET['userID'] : '';
		// $listID used for when there is a delete occuring
		$listID = isset($_GET['listID']) ? $_GET['listID'] : '';
		
		$list_title = isset($_GET['title']) ? $_GET['title'] : '';
		$isDeleted = isset($_GET['isDeleted']) ? $_GET['isDeleted'] : '';  // place holder for later assignment

		
		if ($command == "station") {
			$select_sql = "SELECT lists.listID, lists.title, lists.isDeleted FROM lists JOIN listRights ON lists.listID = listRights.listID WHERE listRights.userID = " . "'$userID'";
			$list_query = $db->query($select_sql);
			$lists = $list_query->fetchAll(PDO::FETCH_ASSOC);
			if ($lists) {	
				echo json_encode($lists);
			}
		} else if ($command == "delete") {      
			// delete query for listRights
			$sql_deleteListRights = "DELETE FROM listRights WHERE listID = "."'$listID'";
		 
			//attempts to delete listRights
			if ($db->query($sql_deleteListRights)) {
				//echo '{"result": "successfully deleted a record for table listRights"}';
				
				//attempts to delete list
				$sql_deleteList = "DELETE FROM lists WHERE listID = "."'$listID'";
				if ($db->query($sql_deleteList)) {
					echo '{"result": "success"}';
				} else {
					echo '{"result": "fail", "error": "listRight and lists record NOT deleted."}';
				}
            }  
		} else if ($command == "update") { 
			$update_sql = "UPDATE lists SET title = '$list_title', isDeleted = '$isDeleted' WHERE listID = '$listID'";
			//attempts to update lists table 
			if ($db->query($update_sql)) {
					echo '{"result": "success"}';
            } else {
					echo '{"result": "fail, list did NOT update"}';
			}
			
		} else {
			echo '{"result": "fail", "error": "in station.php"}';
		}
		
		$db = null;
	} catch (PDOException $e) {
		$error_message = $e->getMessage();
		echo 'There was an error connecting to the database.';
		echo $error_message;
		exit();
	}

?>
