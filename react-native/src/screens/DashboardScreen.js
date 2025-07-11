import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { auth, db } from '../firebase/config';
import { doc, onSnapshot } from 'firebase/firestore';

export default function DashboardScreen() {
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const uid = auth.currentUser.uid;
    const unsub = onSnapshot(doc(db, 'users', uid), (docSnap) => {
      setUserData(docSnap.data());
    });
    return unsub;
  }, []);

  if (!userData) return <View style={styles.container}><Text>Loading...</Text></View>;

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Goal: {userData.goal}</Text>
      <Text style={styles.text}>Level: {userData.level}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, alignItems: 'center', justifyContent: 'center' },
  text: { fontSize: 18, marginVertical: 4 }
});
