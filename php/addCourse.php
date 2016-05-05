<?PHP
ini_set('display_errors', '1');
error_reporting(E_ALL);

	    // Connect to the Database
	    $dsn = 'mysql:host=cssgate.insttech.washington.edu;dbname=_450atm10';
    	$username = '_450atm10';
    	$password = 'afimmit';
       
    	try {
        	$db = new PDO($dsn, $username, $password);
            $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            //get inputs for course information
            $course_id = isset($_GET['id']) ? $_GET['id'] : '';
            $course_short_desc = isset($_GET['shortDesc']) ? $_GET['shortDesc'] : '';
            $course_long_desc = isset($_GET['longDesc']) ? $_GET['longDesc'] : '';
            $course_prereqs = isset($_GET['prereqs']) ? $_GET['prereqs'] : '';
  
            if (strlen($course_id) < 1 
                    || strlen($course_short_desc) < 1 
                    || strlen($course_long_desc) < 1 
                    || strlen($course_prereqs) < 1) {
                echo '{"result": "fail", "error": "Please enter valid data)."}';
            } else {    

                //build query
                $sql = "INSERT INTO Courses";
                $sql .= " VALUES ('$course_id', '$course_short_desc', '$course_long_desc', '$course_prereqs')";
             
                //attempts to add record
                if ($db->query($sql)) {
                    echo '{"result": "success"}';
                    $db = null;
                } 
            }   
        } catch(PDOException $e) {
                if ((int)($e->getCode()) == 23000) {
                    echo '{"result": "fail", "error": "That course already exists."}';
                } else {
                    echo 'Error Number: ' . $e->getCode() . '<br>';
                    echo '{"result": "fail", "error": "Unknown error (' . (((int)($e->getCode()) + 123) * 2) .')"}';
                }
        }
?>
