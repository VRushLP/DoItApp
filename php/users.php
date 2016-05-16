<?PHP
    ini_set('display_errors', '1');
    error_reporting(E_ALL);

	// Connect to the Database
	$dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450atm10';
    $username = '_450atm10';
    $password = 'afimmit';

    try {
        $db = new PDO($dsn, $username, $password);
		$select_sql = 'SELECT email, pwd FROM users';
		$user_query = $db->query($select_sql);
		$users = $user_query->fetchAll(PDO::FETCH_ASSOC);
		if ($users) {	
   			echo json_encode($users);
		}
		$db = null;
    } catch (PDOException $e) {
        	$error_message = $e->getMessage();
        	echo 'There was an error connecting to the database.';
		echo $error_message;
        	exit();
    }
?>
