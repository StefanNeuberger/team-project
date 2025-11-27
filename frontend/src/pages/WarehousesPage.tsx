import { useParams } from "react-router-dom";

export default function WarehousesPage() {
    const { shopId } = useParams();
    return <>Shop { shopId }</>
}