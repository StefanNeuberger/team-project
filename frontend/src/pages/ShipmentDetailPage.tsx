import { useParams } from "react-router-dom";

export default function ShipmentDetailPage() {
    const { shopId, id } = useParams();
    return <>Shop { shopId }, Detail { id }</>
}