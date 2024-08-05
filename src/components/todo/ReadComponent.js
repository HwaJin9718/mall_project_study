import React, {useEffect, useState} from 'react';
import {getOne} from "../../api/todoApi";

const initState = {
    tno : 0,
    title : '',
    write : '',
    dueDate : '',
    complete : false
}

function ReadComponent({tno}) {

    const [todo, setTodo] = useState(initState)

    useEffect(() => {

        getOne(tno).then(data => {
            console.log(data)
            setTodo(data)
        })

    }, [tno]);

    return (
        <div></div>
    );
}

export default ReadComponent;
