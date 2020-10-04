<?PHP
	$name = $_GET['rname'];
	$email = $_GET['remail'];
	$gender = $_GET['rgender'];
	$pass1 = $_GET['renterPass'];
	$pass2 = $_GET['rconfirmPass'];
	// Database Connection code
	$servername = "localhost";
	$username = "id8302626_tarique";
	$password = "tarique123";
	$dbname = "id8302626_reg";
	$con = mysqli_connect($servername,$username,$password,$dbname);
	if(!$con)
	{
		die("Error : ".mysqli_connect_error());
	}
	if($pass1 == $pass2)
	{
		$sql = "INSERT INTO `registration`(`name`, `email`, `gender`, `password`) VALUES('$name','$email','$gender','$pass1');";
		if(mysqli_query($con,$sql))
		{
			echo "Registration Done Successfully...";
		}
		else
		{
			echo "Something went Wrong...";
		}
	}else
	{
		echo "Password Not matched....";
	}
	
	mysqli_close($con);
?>