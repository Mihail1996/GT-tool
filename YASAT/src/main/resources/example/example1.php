<?php

$tainted_id = "taintedId";

function first($id)
{

    $sql = "SELECT username FROM users WHERE id =  $id";

    $non_tainted = ("asasdas"+1222) + "asdasd";

    $sql = "Select";

    $result = mysql_query($sql);
 
    return $result;
}


function another_function($input, $expr)
{
    return first("sdfs");
}

$tainted_value = first($tainted_id);

