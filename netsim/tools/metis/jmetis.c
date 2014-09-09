#include <jmetis.h>
#include <stdlib.h>
#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT jint JNICALL Java_jprime_JMetis_PartitionGraph(
		JNIEnv * env,
		jobject obj,
		jboolean j_force_PartGraphRecursive,
		jboolean j_force_PartGraphKway,
		jint j_num_vertices,
		jintArray j_xadj,
		jintArray j_adjncy,
		jintArray j_vwgt,
		jintArray j_adjwgt,
		jint j_wgtflag,
		jint j_nparts,
		jintArray j_options,
		jintArray j_partioned_nodes) {
	mysrand(7654321L);
	//delcare vars
	jint * xadj=NULL, * adjncy=NULL,* vwgt=NULL, * adjwgt=NULL, * options=NULL, * partioned_nodes=NULL;
	int idx, edges_cut=-1, num_vertices=0, nparts=0, wgtflag=0, numflag=0;
	jsize xadj_len, adjncy_len, vwgt_len, adjwgt_len, options_len, partioned_nodes_len;

	//copy the jsize vars
	num_vertices=j_num_vertices;
	nparts=j_nparts;
	wgtflag=j_wgtflag;

	//get array lengths
	xadj_len            = (*env)->GetArrayLength(env,j_xadj);
	adjncy_len          = (*env)->GetArrayLength(env,j_adjncy);
	vwgt_len            = (*env)->GetArrayLength(env,j_vwgt);
	adjwgt_len          = (*env)->GetArrayLength(env,j_adjwgt);
	options_len         = (*env)->GetArrayLength(env,j_options);
	partioned_nodes_len = (*env)->GetArrayLength(env,j_partioned_nodes);
	//printf("xadj_len=%i, adjncy_len=%i, vwgt_len=%i, adjwgt_len=%i, options_len=%i, partioned_nodes_len=%i\n",xadj_len, adjncy_len, vwgt_len, adjwgt_len, options_len, partioned_nodes_len);

	//create/get local copies
	xadj                      = (*env)->GetIntArrayElements(env,j_xadj,0);
	adjncy                    = (*env)->GetIntArrayElements(env,j_adjncy,0);
	if(vwgt_len>0)    vwgt    = (*env)->GetIntArrayElements(env,j_vwgt,0);
	if(adjwgt_len>0)  adjwgt  = (*env)->GetIntArrayElements(env,j_adjwgt,0);
	if(options_len>0) options = (*env)->GetIntArrayElements(env,j_options,0);
	partioned_nodes           = (int*)malloc(sizeof(int)*partioned_nodes_len);
#if 0
	printf("num_vertices=%i,wgtflag=%i, numflag=%i, nparts=%i\n", num_vertices, wgtflag, numflag, nparts);
	printf("xadj=%p, adjncy=%p, vwgt=%p, adjwgt=%p, options=%p, partioned_nodes=%p\n",
		xadj,
		adjncy,
		vwgt,
		adjwgt,
		options,
		partioned_nodes);
	printf("xadj:");
	for(idx=0;idx<xadj_len;idx++) {
		printf("%i ",xadj[idx]);
	}
	printf("\n");

	printf("adjncy:");
	for(idx=0;idx<adjncy_len;idx++) {
		printf("%i ",adjncy[idx]);
	}
	printf("\n");
#endif
	//call func
	if((j_nparts<8 || j_force_PartGraphRecursive) && !j_force_PartGraphKway) {
		METIS_PartGraphRecursive(
				&num_vertices,
				xadj,
				adjncy,
				vwgt,
				adjwgt,
				&wgtflag,
				&numflag,
				&nparts,
				options,
				&edges_cut,
				partioned_nodes);
	}
	else {
		METIS_PartGraphKway(
				&num_vertices,
				xadj,
				adjncy,
				vwgt,
				adjwgt,
				&wgtflag,
				&numflag,
				&nparts,
				options,
				&edges_cut,
				partioned_nodes);
	}

	//pop partioned_nodes
	(*env)->SetIntArrayRegion(env,j_partioned_nodes,0,partioned_nodes_len,partioned_nodes);

	//free local copies
	free(partioned_nodes);
	(*env)->ReleaseIntArrayElements(env, j_xadj, xadj, 0);
	(*env)->ReleaseIntArrayElements(env, j_adjncy, adjncy, 0);
	if(vwgt_len>0) (*env)->ReleaseIntArrayElements(env, j_vwgt, vwgt, 0);
	if(adjwgt_len>0) (*env)->ReleaseIntArrayElements(env, j_adjwgt, adjwgt, 0);
	if(options_len>0) (*env)->ReleaseIntArrayElements(env, j_options, options, 0);
	return edges_cut;
}

JNIEXPORT jint JNICALL Java_jprime_JMetis_mcPartitionGraph(
		JNIEnv * env,
		jobject obj,
		jboolean j_force_PartGraphRecursive,
		jboolean j_force_PartGraphKway,
		jint j_num_vertices,
		jint j_num_constraints,
		jintArray j_xadj,
		jintArray j_adjncy,
		jintArray j_vwgt,
		jintArray j_adjwgt,
		jint j_wgtflag,
		jint j_nparts,
		jintArray j_options,
		jintArray j_partioned_nodes) {
	mysrand(7654321L);
	//delcare vars
	jint * xadj=NULL, * adjncy=NULL,* vwgt=NULL, * adjwgt=NULL, * options=NULL, * partioned_nodes=NULL;
	int idx, edges_cut=-1, num_vertices=0, num_constraints, nparts=0, wgtflag=0, numflag=0;
	float* ubvec=0;
	jsize xadj_len, adjncy_len, vwgt_len, adjwgt_len, options_len, partioned_nodes_len;

	//copy the jsize vars
	num_vertices=j_num_vertices;
	num_constraints=j_num_constraints;
	nparts=j_nparts;
	wgtflag=j_wgtflag;

	//get array lengths
	xadj_len            = (*env)->GetArrayLength(env,j_xadj);
	adjncy_len          = (*env)->GetArrayLength(env,j_adjncy);
	vwgt_len            = (*env)->GetArrayLength(env,j_vwgt);
	adjwgt_len          = (*env)->GetArrayLength(env,j_adjwgt);
	options_len         = (*env)->GetArrayLength(env,j_options);
	partioned_nodes_len = (*env)->GetArrayLength(env,j_partioned_nodes);

	//create/get local copies
	xadj                      = (*env)->GetIntArrayElements(env,j_xadj,0);
	adjncy                    = (*env)->GetIntArrayElements(env,j_adjncy,0);
	if(vwgt_len>0)    vwgt    = (*env)->GetIntArrayElements(env,j_vwgt,0);
	if(adjwgt_len>0)  adjwgt  = (*env)->GetIntArrayElements(env,j_adjwgt,0);
	if(options_len>0) options = (*env)->GetIntArrayElements(env,j_options,0);
	partioned_nodes           = (int*)malloc(sizeof(int)*partioned_nodes_len);

	//call func
	if((j_nparts<8 || j_force_PartGraphRecursive) && !j_force_PartGraphKway) {
		//printf("[0]nparts=%i, num_vertices=%i, num_constraints=%i, wgtflag=%i, xadj_len=%i, adjncy_len=%i, vwgt_len=%i, adjwgt_len=%i, options_len=%i, partioned_nodes_len=%i\n",
		//		nparts, num_vertices, num_constraints, wgtflag, xadj_len, adjncy_len, vwgt_len, adjwgt_len, options_len, partioned_nodes_len);
		METIS_mCPartGraphRecursive(
				&num_vertices,
				&num_constraints,
				xadj,
				adjncy,
				vwgt,
				adjwgt,
				&wgtflag,
				&numflag,
				&nparts,
				options,
				&edges_cut,
				partioned_nodes);
	}
	else {
		//printf("[1]nparts=%i, num_vertices=%i, num_constraints=%i, wgtflag=%i, xadj_len=%i, adjncy_len=%i, vwgt_len=%i, adjwgt_len=%i, options_len=%i, partioned_nodes_len=%i\n",
		//		nparts, num_vertices, num_constraints, wgtflag, xadj_len, adjncy_len, vwgt_len, adjwgt_len, options_len, partioned_nodes_len);
		ubvec = (float*)malloc(sizeof(float)*num_constraints);
		for(idx=0;idx<num_constraints;idx++) {
			ubvec[idx]=1.1; //manual says should be larger than 1.03 so 1.1 seems reasonable
		}
		METIS_mCPartGraphKway(
				&num_vertices, //int *nvtxs,
				&num_constraints,//int *ncon,
				xadj,//idxtype *xadj,
				adjncy,//idxtype *adjncy,
				vwgt,//idxtype *vwgt,
				adjwgt,//idxtype *adjwgt,
				&wgtflag,//int *wgtflag,
				&numflag,//int *numflag,
				&nparts, //int *nparts,
				ubvec,//float *rubvec,
				options,//int *options,
				&edges_cut,//int *edgecut,
				partioned_nodes); //idxtype *part)
		free(ubvec);
	}

	//pop partioned_nodes
	(*env)->SetIntArrayRegion(env,j_partioned_nodes,0,partioned_nodes_len,partioned_nodes);

	//free local copies
	free(partioned_nodes);
	(*env)->ReleaseIntArrayElements(env, j_xadj, xadj, 0);
	(*env)->ReleaseIntArrayElements(env, j_adjncy, adjncy, 0);
	if(vwgt_len>0) (*env)->ReleaseIntArrayElements(env, j_vwgt, vwgt, 0);
	if(adjwgt_len>0) (*env)->ReleaseIntArrayElements(env, j_adjwgt, adjwgt, 0);
	if(options_len>0) (*env)->ReleaseIntArrayElements(env, j_options, options, 0);
	return edges_cut;
}

JNIEXPORT jint JNICALL Java_jprime_JMetis_wPartitionGraph(
		JNIEnv * env,
		jobject obj,
		jboolean j_force_PartGraphRecursive,
		jboolean j_force_PartGraphKway,
		jint j_num_vertices,
		jintArray j_xadj,
		jintArray j_adjncy,
		jintArray j_vwgt,
		jintArray j_adjwgt,
		jint j_wgtflag,
		jint j_nparts,
		jfloatArray j_part_weights,
		jintArray j_options,
		jintArray j_partioned_nodes){
	mysrand(7654321L);
	//delcare vars
	jint * xadj=NULL, * adjncy=NULL,* vwgt=NULL, * adjwgt=NULL, * options=NULL, * partioned_nodes=NULL;
	jfloat* part_weights=NULL;
	int idx, edges_cut=-1, num_vertices=0, nparts=0, wgtflag=0, numflag=0;
	jsize xadj_len, adjncy_len, vwgt_len, adjwgt_len, options_len, partioned_nodes_len, part_weights_len;

	//copy the jsize vars
	num_vertices=j_num_vertices;
	nparts=j_nparts;
	wgtflag=j_wgtflag;

	//get array lengths
	xadj_len            = (*env)->GetArrayLength(env,j_xadj);
	adjncy_len          = (*env)->GetArrayLength(env,j_adjncy);
	vwgt_len            = (*env)->GetArrayLength(env,j_vwgt);
	adjwgt_len          = (*env)->GetArrayLength(env,j_adjwgt);
	options_len         = (*env)->GetArrayLength(env,j_options);
	partioned_nodes_len = (*env)->GetArrayLength(env,j_partioned_nodes);
	part_weights_len    = (*env)->GetArrayLength(env,j_part_weights);

	//create/get local copies
	xadj                      = (*env)->GetIntArrayElements(env,j_xadj,0);
	adjncy                    = (*env)->GetIntArrayElements(env,j_adjncy,0);
	part_weights              = (*env)->GetFloatArrayElements(env,j_part_weights,0);
	if(vwgt_len>0)    vwgt    = (*env)->GetIntArrayElements(env,j_vwgt,0);
	if(adjwgt_len>0)  adjwgt  = (*env)->GetIntArrayElements(env,j_adjwgt,0);
	if(options_len>0) options = (*env)->GetIntArrayElements(env,j_options,0);
	partioned_nodes           = (int*)malloc(sizeof(int)*partioned_nodes_len);

	//call func
	if((j_nparts<8 || j_force_PartGraphRecursive) && !j_force_PartGraphKway) {
		METIS_WPartGraphRecursive(
				&num_vertices,
				xadj,
				adjncy,
				vwgt,
				adjwgt,
				&wgtflag,
				&numflag,
				&nparts,
				part_weights,
				options,
				&edges_cut,
				partioned_nodes);
	}
	else {
		METIS_WPartGraphKway(
				&num_vertices,
				xadj,
				adjncy,
				vwgt,
				adjwgt,
				&wgtflag,
				&numflag,
				&nparts,
				part_weights,
				options,
				&edges_cut,
				partioned_nodes);
	}

	//pop partioned_nodes
	(*env)->SetIntArrayRegion(env,j_partioned_nodes,0,partioned_nodes_len,partioned_nodes);

	//free local copies
	free(partioned_nodes);
	(*env)->ReleaseIntArrayElements(env, j_xadj, xadj, 0);
	(*env)->ReleaseIntArrayElements(env, j_adjncy, adjncy, 0);
	if(vwgt_len>0) (*env)->ReleaseIntArrayElements(env, j_vwgt, vwgt, 0);
	if(adjwgt_len>0) (*env)->ReleaseIntArrayElements(env, j_adjwgt, adjwgt, 0);
	if(options_len>0) (*env)->ReleaseIntArrayElements(env, j_options, options, 0);
	return edges_cut;
}

#ifdef __cplusplus
}
#endif
