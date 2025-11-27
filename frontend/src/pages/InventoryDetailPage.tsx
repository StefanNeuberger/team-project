import { useParams } from "react-router-dom";

export default function InventoryDetailPage() {
    const { shopId, id } = useParams();
    return <>Shop { shopId }, Detail { id }</>
}