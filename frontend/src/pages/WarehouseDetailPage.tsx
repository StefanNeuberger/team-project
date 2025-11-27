import { useParams } from "react-router-dom";

export default function WarehouseDetailPage() {
    const { shopId, id } = useParams();
    return <>Shop { shopId }, Detail { id }</>
}