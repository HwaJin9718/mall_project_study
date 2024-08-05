import React, { useCallback } from "react";
import BasicLayout from "../../layouts/BasicLayout";
import { Outlet, useNavigate } from "react-router-dom";

function IndexPage() {

    const navigete = useNavigate()

    const handleClickList = useCallback(() => {
        navigete({pathname:'list'})
    },[])

    const handleClickAdd = useCallback(() => {
        navigete({pathname:'add'})
    },[])
    
    return (
        <BasicLayout>

            <div className="w-full flex m-2 p-2 ">
                <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline" onClick={handleClickList}>LIST</div>
                <div className="text-xl m-1 p-2 w-20 font-extrabold text-center underline" onClick={handleClickAdd}>ADD</div>
            </div>

            <div className="flex flex-wrap w-full">
                <Outlet/> {/* MainPage 내 서브 메뉴 설정 */}
            </div>

        </BasicLayout>
    );
}

export default IndexPage;
