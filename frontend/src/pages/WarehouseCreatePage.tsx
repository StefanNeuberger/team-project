import WarehouseCreateForm from "@/components/warehouse/WarehouseCreateForm.tsx";
import { useParams } from "react-router-dom";

export default function WarehouseCreatePage() {
    const { shopId } = useParams();
    return <div className="p-8 flex flex-col justify-start items-center">
        <WarehouseCreateForm shopId={ shopId! }/>
    </div>
}